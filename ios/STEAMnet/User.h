//
//  User.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface User : NSManagedObject

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * remoteId;
@property (nonatomic, retain) NSDate * createdDate;
@property (nonatomic, retain) NSDate * updatedDate;
@property (nonatomic, retain) NSSet *comments;
@property (nonatomic, retain) NSSet *ideas;
@property (nonatomic, retain) NSSet *sparks;
@end

@interface User (CoreDataGeneratedAccessors)

- (void)addCommentsObject:(NSManagedObject *)value;
- (void)removeCommentsObject:(NSManagedObject *)value;
- (void)addComments:(NSSet *)values;
- (void)removeComments:(NSSet *)values;

- (void)addIdeasObject:(NSManagedObject *)value;
- (void)removeIdeasObject:(NSManagedObject *)value;
- (void)addIdeas:(NSSet *)values;
- (void)removeIdeas:(NSSet *)values;

- (void)addSparksObject:(NSManagedObject *)value;
- (void)removeSparksObject:(NSManagedObject *)value;
- (void)addSparks:(NSSet *)values;
- (void)removeSparks:(NSSet *)values;

@end