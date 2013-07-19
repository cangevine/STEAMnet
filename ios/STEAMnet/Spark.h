//
//  Spark.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Comment, Idea, Tag, User;

@interface Spark : NSManagedObject

@property (nonatomic, retain) NSString * content;
@property (nonatomic, retain) NSString * contentType;
@property (nonatomic, retain) NSDate * createdDate;
@property (nonatomic, retain) NSNumber * remoteId;
@property (nonatomic, retain) NSString * sparkType;
@property (nonatomic, retain) NSSet *comments;
@property (nonatomic, retain) NSSet *ideas;
@property (nonatomic, retain) NSSet *tags;
@property (nonatomic, retain) NSSet *users;
@end

@interface Spark (CoreDataGeneratedAccessors)

- (void)addCommentsObject:(Comment *)value;
- (void)removeCommentsObject:(Comment *)value;
- (void)addComments:(NSSet *)values;
- (void)removeComments:(NSSet *)values;

- (void)addIdeasObject:(Idea *)value;
- (void)removeIdeasObject:(Idea *)value;
- (void)addIdeas:(NSSet *)values;
- (void)removeIdeas:(NSSet *)values;

- (void)addTagsObject:(Tag *)value;
- (void)removeTagsObject:(Tag *)value;
- (void)addTags:(NSSet *)values;
- (void)removeTags:(NSSet *)values;

- (void)addUsersObject:(User *)value;
- (void)removeUsersObject:(User *)value;
- (void)addUsers:(NSSet *)values;
- (void)removeUsers:(NSSet *)values;

@end
