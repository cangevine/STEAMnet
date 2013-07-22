//
//  Idea.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

#import "Jawn.h"

@class Comment, Tag, User;

@interface Idea : Jawn

@property (nonatomic, retain) NSString * descriptionText;
@property (nonatomic, retain) NSSet *sparks;
@property (nonatomic, retain) User *user;
@end

@interface Idea (CoreDataGeneratedAccessors)

- (void)addSparksObject:(NSManagedObject *)value;
- (void)removeSparksObject:(NSManagedObject *)value;
- (void)addSparks:(NSSet *)values;
- (void)removeSparks:(NSSet *)values;

@end
