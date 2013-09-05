//
//  SNJawnsViewController.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/15/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#define kBgQueue dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)

#import "SNAPIHelper.h"
#import "NSDate+RailsDates.h"
#import "SNAppDelegate.h"

#import "SNJawnsViewController.h"

#import "Jawn.h"
#import "Spark.h"
#import "Idea.h"

#import "SNSparkCell.h"
#import "SNIdeaCell.h"

@interface SNJawnsViewController ()

- (void)saveContext;
- (void)fetchData;
- (void)parseData:(NSData *)returnedData;

@property (nonatomic, strong) UIRefreshControl *refreshControl;
@property (nonatomic, strong) NSMutableArray *objectChanges;

@end

@implementation SNJawnsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	
    SNAppDelegate *delegate = [[UIApplication sharedApplication] delegate];
    self.managedObjectContext = [delegate managedObjectContext];
    
    self.refreshControl = [[UIRefreshControl alloc] init];
    [self.refreshControl addTarget:self action:@selector(fetchData) forControlEvents:UIControlEventValueChanged];
    [self.collectionView addSubview:self.refreshControl];
    
//    self.collectionView.backgroundColor = [UIColor colorWithRed:0.75 green:0.75 blue:0.75 alpha:1.0];
    self.collectionView.backgroundColor = [UIColor blackColor];
    
    self.title = @"STEAMnet";
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    if (![defaults valueForKey:@"token"]) {
        SNLoginViewController *vc = [[SNLoginViewController alloc] init];
        UINavigationController *nc = [[UINavigationController alloc] initWithRootViewController:vc];
        
        vc.delegate = self;
        
        [self presentViewController:nc animated:YES completion:nil];
    } else {
        [self fetchData];
    }
    
    [[self collectionView] registerClass:[SNSparkCell class] forCellWithReuseIdentifier:@"SparkCell"];
    [[self collectionView] registerClass:[SNIdeaCell class] forCellWithReuseIdentifier:@"IdeaCell"];
}

- (void)didLogIn:(BOOL)loggedIn
{
    if (loggedIn) {
        [self fetchData];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake(145, 145);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(10, 10, 10, 10);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 10;
}

- (NSInteger)collectionView:(UICollectionView *)view numberOfItemsInSection:(NSInteger)section {
    id <NSFetchedResultsSectionInfo> sectionInfo = [self.fetchedResultsController sections][section];
    return [sectionInfo numberOfObjects];
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)cv cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    UICollectionViewCell *cell;
    
    Jawn *jawn = [self.fetchedResultsController objectAtIndexPath:indexPath];
    
    if ([jawn entity] == [NSEntityDescription entityForName:@"Idea" inManagedObjectContext:self.managedObjectContext]) {
        SNIdeaCell *ideaCell = (SNIdeaCell *)[cv dequeueReusableCellWithReuseIdentifier:@"IdeaCell" forIndexPath:indexPath];
        ideaCell.idea = (Idea *)jawn;
        
        cell = ideaCell;
    } else if ([jawn entity] == [NSEntityDescription entityForName:@"Spark" inManagedObjectContext:self.managedObjectContext]) {
        SNSparkCell *sparkCell = (SNSparkCell *)[cv dequeueReusableCellWithReuseIdentifier:@"SparkCell" forIndexPath:indexPath];
        sparkCell.spark = (Spark *)jawn;
        
        cell = sparkCell;
    }
    
    return cell;
}

- (void)fetchData
{
    dispatch_async(kBgQueue, ^{
        NSData *data = [NSData dataWithContentsOfURL:[SNAPIHelper urlForPath:@"jawns" options:@{@"lite": @"true"}]];
        [self performSelectorOnMainThread:@selector(parseData:) withObject:data waitUntilDone:YES];
    });
}

- (void)parseData:(NSData *)returnedData
{
    [self.refreshControl endRefreshing];
    
    NSMutableArray *ideasToDelete = [[Idea savedRemoteIds] mutableCopy];
    NSMutableArray *sparksToDelete = [[Spark savedRemoteIds] mutableCopy];
    
    NSError *error;
    NSArray *tempJawns = [NSJSONSerialization JSONObjectWithData:returnedData options:0 error:&error];
    
    if (error) {
        NSLog(@"%@", error.description);
    }
    
    for (NSDictionary *jawnDict in tempJawns) {
        if ([jawnDict[@"jawn_type"] isEqualToString:@"spark"]) {
            [sparksToDelete removeObject:jawnDict[@"id"]];
            
            Spark *spark;
            
            if ([Spark jawnExistsWithRemoteId:jawnDict[@"id"]]) {
                spark = (Spark *)[Spark jawnWithRemoteId:jawnDict[@"id"]];
            } else {
                // Only create a new spark if it doesn't already exist
                spark = [NSEntityDescription insertNewObjectForEntityForName:@"Spark" inManagedObjectContext:self.managedObjectContext];
                
                spark.remoteId = jawnDict[@"id"];
                spark.createdDate = [NSDate dateWithISO8601String:jawnDict[@"created_at"]];
            }
            
            if (jawnDict[@"file"]) {
                spark.fileURL = jawnDict[@"file"];
            } else {
                spark.fileURL = @"";
            }
            spark.sparkType = jawnDict[@"spark_type"];
            spark.contentType = jawnDict[@"content_type"];
            spark.content = jawnDict[@"content"];
            
            spark.cacheUpdated = [NSDate date];
        } else if ([jawnDict[@"jawn_type"] isEqualToString:@"idea"]) {
            [ideasToDelete removeObject:jawnDict[@"id"]];
            
            Idea *idea;
            
            if ([Idea jawnExistsWithRemoteId:jawnDict[@"id"]]) {
                idea = (Idea *)[Idea jawnWithRemoteId:jawnDict[@"id"]];
            } else {
                // Only create a new idea if it doesn't already exist
                idea = [NSEntityDescription insertNewObjectForEntityForName:@"Idea" inManagedObjectContext:self.managedObjectContext];
                
                idea.remoteId = jawnDict[@"id"];
                idea.createdDate = [NSDate dateWithISO8601String:jawnDict[@"created_at"]];
            }
            
            idea.descriptionText = jawnDict[@"description"];
            
            idea.cacheUpdated = [NSDate date];
        }
    }
    
    [Idea deleteJawnsWithRemoteIds:ideasToDelete];
    [Spark deleteJawnsWithRemoteIds:sparksToDelete];
    
    [self saveContext];
}

- (void)saveContext
{
    NSError *error;
    if (![self.managedObjectContext save:&error]) {
        NSLog(@"Whoops, couldn't save: %@", [error localizedDescription]);
    }
}

#pragma mark - Fetched results controller

- (NSFetchedResultsController *)fetchedResultsController
{
    if (_fetchedResultsController != nil) {
        return _fetchedResultsController;
    }
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    // Edit the entity name as appropriate.
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"Jawn" inManagedObjectContext:self.managedObjectContext];
    [fetchRequest setEntity:entity];
    
    // Set the batch size to a suitable number.
    [fetchRequest setFetchBatchSize:22];
    
    // Edit the sort key as appropriate.
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"createdDate" ascending:NO];
    NSArray *sortDescriptors = @[sortDescriptor];
    
    [fetchRequest setSortDescriptors:sortDescriptors];
    
    // Edit the section name key path and cache name if appropriate.
    // nil for section name key path means "no sections".
    NSFetchedResultsController *aFetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest managedObjectContext:self.managedObjectContext sectionNameKeyPath:nil cacheName:@"Master"];
    aFetchedResultsController.delegate = self;
    self.fetchedResultsController = aFetchedResultsController;
    
	NSError *error = nil;
	if (![self.fetchedResultsController performFetch:&error]) {
        // Replace this implementation with code to handle the error appropriately.
        // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
	    NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
	    abort();
	}
    
    return _fetchedResultsController;
}

- (void)controllerWillChangeContent:(NSFetchedResultsController *)controller
{
    self.objectChanges = [[NSMutableArray alloc] init];
}

- (void)controller:(NSFetchedResultsController *)controller didChangeSection:(id <NSFetchedResultsSectionInfo>)sectionInfo atIndex:(NSUInteger)sectionIndex forChangeType:(NSFetchedResultsChangeType)type
{
    NSLog(@"didChangeSection");
    switch(type) {
        case NSFetchedResultsChangeInsert:
            [self.collectionView insertSections:[NSIndexSet indexSetWithIndex:sectionIndex]];
            break;
            
        case NSFetchedResultsChangeDelete:
            [self.collectionView deleteSections:[NSIndexSet indexSetWithIndex:sectionIndex]];
            break;
    }
}

- (void)controller:(NSFetchedResultsController *)controller didChangeObject:(id)anObject atIndexPath:(NSIndexPath *)indexPath forChangeType:(NSFetchedResultsChangeType)type newIndexPath:(NSIndexPath *)newIndexPath
{
    NSMutableDictionary *change = [[NSMutableDictionary alloc] init];
    
    switch(type) {
        case NSFetchedResultsChangeInsert:
//            [self.collectionView insertItemsAtIndexPaths:@[newIndexPath]];
            change[@(type)] = newIndexPath;
            break;
            
        case NSFetchedResultsChangeDelete:
//            [self.collectionView deleteItemsAtIndexPaths:@[indexPath]];
            change[@(type)] = indexPath;
            break;
            
        case NSFetchedResultsChangeUpdate:
//            [self configureCell:[tableView cellForRowAtIndexPath:indexPath] atIndexPath:indexPath];
            change[@(type)] = indexPath;
            break;
            
        case NSFetchedResultsChangeMove:
//            [self.collectionView deleteItemsAtIndexPaths:@[indexPath]];
//            [self.collectionView insertItemsAtIndexPaths:@[newIndexPath]];
            change[@(type)] = @[indexPath, newIndexPath];
            break;
    }
    
    [self.objectChanges addObject:change];
}

- (void)controllerDidChangeContent:(NSFetchedResultsController *)controller
{
    if ([self.objectChanges count] > 5) {
        [self.collectionView reloadData];
    } else {
        [self.collectionView performBatchUpdates:^{
            for (NSDictionary *change in self.objectChanges) {
                [change enumerateKeysAndObjectsUsingBlock:^(NSNumber *key, id obj, BOOL *stop) {
                    NSFetchedResultsChangeType type = [key unsignedIntegerValue];
                    switch (type) {
                        case NSFetchedResultsChangeInsert:
                            [self.collectionView insertItemsAtIndexPaths:@[obj]];
                            break;
                            
                        case NSFetchedResultsChangeDelete:
                            [self.collectionView deleteItemsAtIndexPaths:@[obj]];
                            break;
                            
                        case NSFetchedResultsChangeUpdate:
                            [self.collectionView reloadItemsAtIndexPaths:@[obj]];
                            break;
                            
                        case NSFetchedResultsChangeMove:
                            [self.collectionView moveItemAtIndexPath:obj[0] toIndexPath:obj[1]];
                            break;
                    }
                }];
            }
        } completion:nil];
    }
    
    [self.objectChanges removeAllObjects];
}

@end
