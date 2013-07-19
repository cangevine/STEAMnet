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

- (void)fetchData;
- (void)parseData:(NSData *)returnedData;

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
    
    self.collectionView.backgroundColor = [UIColor colorWithRed:0.9 green:0.9 blue:0.9 alpha:1.0];
    
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
    return CGSizeMake(130, 130);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(20, 20, 20, 20);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 20;
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
    NSError *error = nil;
    NSArray *tempJawns = [NSJSONSerialization JSONObjectWithData:returnedData options:0 error:&error];
    
    if (error) {
        NSLog(@"%@", error.description);
    }
    
    for (NSDictionary *jawnDict in tempJawns) {
        if ([jawnDict[@"jawn_type"] isEqualToString:@"spark"]) {
            NSFetchRequest *request = [[NSFetchRequest alloc] init];
            [request setFetchLimit:1];
            
            NSEntityDescription *entity = [NSEntityDescription entityForName:@"Spark" inManagedObjectContext:self.managedObjectContext];
            [request setEntity:entity];
            
            NSPredicate *predicate = [NSPredicate predicateWithFormat:@"remoteId == %@", jawnDict[@"id"]];
            [request setPredicate:predicate];
            
            NSError *error;
            NSInteger count = [self.managedObjectContext countForFetchRequest:request error:&error];
            if (count < 1) {
                // Only create a new spark if it doesn't already exist
                
                Spark *spark = [NSEntityDescription insertNewObjectForEntityForName:@"Spark" inManagedObjectContext:self.managedObjectContext];
                spark.remoteId = jawnDict[@"id"];
                spark.sparkType = jawnDict[@"spark_type"];
                spark.contentType = jawnDict[@"content_type"];
                spark.content = jawnDict[@"content"];
                spark.createdDate = [NSDate dateWithISO8601String:jawnDict[@"created_at"]];
            }
        } else if ([jawnDict[@"jawn_type"] isEqualToString:@"idea"]) {
            NSFetchRequest *request = [[NSFetchRequest alloc] init];
            [request setFetchLimit:1];
            
            NSEntityDescription *entity = [NSEntityDescription entityForName:@"Idea" inManagedObjectContext:self.managedObjectContext];
            [request setEntity:entity];
            
            NSPredicate *predicate = [NSPredicate predicateWithFormat:@"remoteId == %@", jawnDict[@"id"]];
            [request setPredicate:predicate];
            
            NSError *error;
            NSInteger count = [self.managedObjectContext countForFetchRequest:request error:&error];
            if (count < 1) {
                // Only create a new idea if it doesn't already exist
                
                Idea *idea = [NSEntityDescription insertNewObjectForEntityForName:@"Idea" inManagedObjectContext:self.managedObjectContext];
                idea.remoteId = jawnDict[@"id"];
                idea.descriptionText = jawnDict[@"description"];
                idea.createdDate = [NSDate dateWithISO8601String:jawnDict[@"created_at"]];
            }
        }
    }
    
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

- (void)controllerDidChangeContent:(NSFetchedResultsController *)controller
{
    // In the simplest, most efficient, case, reload the table view.
    [self.collectionView reloadData];
}

@end
