//
//  Jawn.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/18/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Comment, Tag;

@interface Jawn : NSManagedObject

@property (nonatomic, retain) NSDate * createdDate;
@property (nonatomic, retain) NSNumber * remoteId;
@property (nonatomic, retain) NSSet *tags;
@property (nonatomic, retain) NSSet *comments;
@end

@interface Jawn (CoreDataGeneratedAccessors)

+ (BOOL)jawnExistsWithRemoteId:(NSNumber *)remoteId;
+ (NSArray *)savedRemoteIds;
+ (void)deleteJawnsWithRemoteIds:(NSArray *)remoteIds;

- (NSURL *)dataURL;

- (void)addTagsObject:(Tag *)value;
- (void)removeTagsObject:(Tag *)value;
- (void)addTags:(NSSet *)values;
- (void)removeTags:(NSSet *)values;

- (void)addCommentsObject:(Comment *)value;
- (void)removeCommentsObject:(Comment *)value;
- (void)addComments:(NSSet *)values;
- (void)removeComments:(NSSet *)values;

@end
