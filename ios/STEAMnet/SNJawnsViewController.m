//
//  SNJawnsViewController.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/15/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#define kBgQueue dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)

#import "SNJawnsViewController.h"

#import "SNSparkCell.h"
#import "SNIdeaCell.h"

#import "SNLoginViewController.h"

@interface SNJawnsViewController ()

- (void)fetchData;
- (void)parseData:(NSData *)returnedData;

@property(nonatomic, strong) NSMutableArray *jawns;

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
	
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    if (![defaults valueForKey:@"token"]) {
        SNLoginViewController *vc = [[SNLoginViewController alloc] init];
        UINavigationController *nc = [[UINavigationController alloc] initWithRootViewController:vc];
        
        [self presentViewController:nc animated:YES completion:nil];
    }
    
    self.collectionView.backgroundColor = [UIColor colorWithRed:0.9 green:0.9 blue:0.9 alpha:1.0];
    
    self.title = @"STEAMnet";
    
    _jawns = [[NSMutableArray alloc] init];
    
    [self fetchData];
    
    [[self collectionView] registerClass:[SNSparkCell class] forCellWithReuseIdentifier:@"SparkCell"];
    [[self collectionView] registerClass:[SNIdeaCell class] forCellWithReuseIdentifier:@"IdeaCell"];
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
    return [_jawns count];
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)cv cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    UICollectionViewCell *cell;
    
    if ([_jawns[indexPath.row] isEqualToString:@"idea"]) {
        cell = [cv dequeueReusableCellWithReuseIdentifier:@"IdeaCell" forIndexPath:indexPath];
    } else if ([_jawns[indexPath.row] isEqualToString:@"spark"]) {
        cell = [cv dequeueReusableCellWithReuseIdentifier:@"SparkCell" forIndexPath:indexPath];
    }
    
    return cell;
}

- (void)fetchData
{
    dispatch_async(kBgQueue, ^{
        NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:@"http://steamnet.herokuapp.com/api/v1/jawns.json"]];
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
    
    for (NSDictionary *jawn in tempJawns) {
        [_jawns addObject:jawn[@"jawn_type"]];
    }
    
    [self.collectionView reloadData];
}

@end
