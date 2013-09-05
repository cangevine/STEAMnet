//
//  Spark.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

#import "Jawn.h"

@class Comment, Idea, Tag, User;

@interface Spark : Jawn

@property (nonatomic, retain) NSString * content;
@property (nonatomic, retain) NSString * contentType;
@property (nonatomic, retain) NSString * sparkType;
@property (nonatomic, retain) NSString * fileURL;
@property (nonatomic, retain) NSSet *ideas;
@property (nonatomic, retain) NSSet *users;

@property (nonatomic, retain) UIImage *cachedImage;

+ (UIColor *)colorForSparkType:(NSString *)sparkType;
+ (UIColor *)colorForContentType:(NSString *)contentType;

@end

@interface Spark (CoreDataGeneratedAccessors)

+ (BOOL)sparkExistsWithRemoteId:(int)sparkId;

- (void)addIdeasObject:(Idea *)value;
- (void)removeIdeasObject:(Idea *)value;
- (void)addIdeas:(NSSet *)values;
- (void)removeIdeas:(NSSet *)values;

- (void)addUsersObject:(User *)value;
- (void)removeUsersObject:(User *)value;
- (void)addUsers:(NSSet *)values;
- (void)removeUsers:(NSSet *)values;

@end
