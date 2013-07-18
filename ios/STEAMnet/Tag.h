//
//  Tag.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Tag : NSManagedObject

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSSet *ideas;
@property (nonatomic, retain) NSSet *sparks;
@end

@interface Tag (CoreDataGeneratedAccessors)

- (void)addIdeasObject:(NSManagedObject *)value;
- (void)removeIdeasObject:(NSManagedObject *)value;
- (void)addIdeas:(NSSet *)values;
- (void)removeIdeas:(NSSet *)values;

- (void)addSparksObject:(NSManagedObject *)value;
- (void)removeSparksObject:(NSManagedObject *)value;
- (void)addSparks:(NSSet *)values;
- (void)removeSparks:(NSSet *)values;

@end
