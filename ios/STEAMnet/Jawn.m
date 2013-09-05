//
//  Jawn.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/18/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "Jawn.h"
#import "Comment.h"
#import "Tag.h"

#import "Spark.h"
#import "Idea.h"

#import "SNAppDelegate.h"
#import "SNAPIHelper.h"

@interface Jawn ()

+ (NSString *)entityName;

@end

@implementation Jawn

@dynamic createdDate;
@dynamic cacheUpdated;
@dynamic remoteId;
@dynamic tags;
@dynamic comments;

+ (BOOL)jawnExistsWithRemoteId:(NSNumber *)remoteId
{
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    [request setFetchLimit:1];
    
    NSManagedObjectContext *context = [(SNAppDelegate *)[[UIApplication sharedApplication] delegate] managedObjectContext];
    
    NSEntityDescription *entity = [NSEntityDescription entityForName:[self entityName] inManagedObjectContext:context];
    [request setEntity:entity];
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"remoteId == %@", remoteId];
    [request setPredicate:predicate];
    
    NSError *error;
    NSInteger count = [context countForFetchRequest:request error:&error];
    
    return (count != 0);
}

+ (NSArray *)savedRemoteIds
{
    NSFetchRequest *fetchRequest = [NSFetchRequest fetchRequestWithEntityName:[self entityName]];
    
    fetchRequest.resultType = NSDictionaryResultType;
    
    [fetchRequest setPropertiesToFetch:[NSArray arrayWithObjects:@"remoteId", nil]];
    
    NSManagedObjectContext *context = [(SNAppDelegate *)[[UIApplication sharedApplication] delegate] managedObjectContext];
    NSError *error = nil;
    NSArray *results = [context executeFetchRequest:fetchRequest error:&error];
    
    return [results valueForKey:@"remoteId"];
}

+ (void)deleteJawnsWithRemoteIds:(NSArray *)remoteIds
{
    NSManagedObjectContext *context = [(SNAppDelegate *)[[UIApplication sharedApplication] delegate] managedObjectContext];
    NSEntityDescription *entity = [NSEntityDescription entityForName:[self entityName] inManagedObjectContext:context];
    
    for (NSNumber *remoteId in remoteIds) {
        NSFetchRequest *request = [[NSFetchRequest alloc] init];
        [request setFetchLimit:1];
        
        [request setEntity:entity];
        
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"remoteId == %@", remoteId];
        [request setPredicate:predicate];
        
        NSError *error;
        NSArray *result = [context executeFetchRequest:request error:&error];
        
        [context deleteObject:(Jawn *)result[0]];
    }
}

+ (Jawn *)jawnWithRemoteId:(NSNumber *)remoteId
{
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    [request setFetchLimit:1];
    
    NSManagedObjectContext *context = [(SNAppDelegate *)[[UIApplication sharedApplication] delegate] managedObjectContext];
    
    NSEntityDescription *entity = [NSEntityDescription entityForName:[self entityName] inManagedObjectContext:context];
    [request setEntity:entity];
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"remoteId == %@", remoteId];
    [request setPredicate:predicate];
    
    NSError *error;
    NSArray *result = [context executeFetchRequest:request error:&error];
    
    return (Jawn *)result[0];
}

+ (NSString *)entityName
{
    NSString *entityName = @"";
    
    if (self == [Spark class]) {
        entityName = @"Spark";
    } else if (self == [Idea class]) {
        entityName = @"Idea";
    }
    
    return entityName;
}

- (NSURL *)dataURL
{
    NSMutableString *path = [[NSMutableString alloc] init];
    
    if (self == [Spark class]) {
        [path appendString:@"sparks/"];
    } else if (self == [Idea class]) {
        [path appendString:@"ideas/"];
    }
    
    [path appendString:[NSString stringWithFormat:@"%@", self.remoteId]];
    
    return [SNAPIHelper urlForPath:path options:nil];
}

@end
