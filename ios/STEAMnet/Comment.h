//
//  Comment.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class User, Spark, Idea;

@interface Comment : NSManagedObject

@property (nonatomic, retain) NSString * text;
@property (nonatomic, retain) NSNumber * remoteId;
@property (nonatomic, retain) NSDate * createdDate;
@property (nonatomic, retain) Idea *idea;
@property (nonatomic, retain) Spark *spark;
@property (nonatomic, retain) User *user;

@property (nonatomic, retain) id jawn;

@end
