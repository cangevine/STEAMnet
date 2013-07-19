//
//  Comment.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "Comment.h"
#import "User.h"
#import "Idea.h"
#import "Spark.h"

@implementation Comment

@dynamic text;
@dynamic remoteId;
@dynamic createdDate;
@dynamic idea;
@dynamic spark;
@dynamic user;

- (id)jawn
{
    if (self.idea) {
        return self.idea;
    } else if (self.spark) {
        return self.spark;
    } else {
        return nil;
    }
}

- (void)setJawn:(id)jawn
{
    if ([jawn isKindOfClass:[Idea class]]) {
        self.idea = jawn;
        self.spark = nil;
    } else if ([jawn isKindOfClass:[Spark class]]) {
        self.spark = jawn;
        self.idea = nil;
    }
}

@end
