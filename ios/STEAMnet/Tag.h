//
//  Tag.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Jawn;

@interface Tag : NSManagedObject

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSSet *jawns;
@end

@interface Tag (CoreDataGeneratedAccessors)

- (void)addJawnsObject:(Jawn *)value;
- (void)removeJawnsObject:(Jawn *)value;
- (void)addJawns:(NSSet *)values;
- (void)removeJawns:(NSSet *)values;

@end
