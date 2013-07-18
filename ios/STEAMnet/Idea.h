//
//  Idea.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Comment, Tag, User;

@interface Idea : NSManagedObject

@property (nonatomic, retain) NSString * descriptionText;
@property (nonatomic, retain) NSNumber * remoteId;
@property (nonatomic, retain) NSDate * createdDate;
@property (nonatomic, retain) NSSet *comments;
@property (nonatomic, retain) NSSet *sparks;
@property (nonatomic, retain) NSSet *tags;
@property (nonatomic, retain) User *user;
@end

@interface Idea (CoreDataGeneratedAccessors)

- (void)addCommentsObject:(Comment *)value;
- (void)removeCommentsObject:(Comment *)value;
- (void)addComments:(NSSet *)values;
- (void)removeComments:(NSSet *)values;

- (void)addSparksObject:(NSManagedObject *)value;
- (void)removeSparksObject:(NSManagedObject *)value;
- (void)addSparks:(NSSet *)values;
- (void)removeSparks:(NSSet *)values;

- (void)addTagsObject:(Tag *)value;
- (void)removeTagsObject:(Tag *)value;
- (void)addTags:(NSSet *)values;
- (void)removeTags:(NSSet *)values;

@end
