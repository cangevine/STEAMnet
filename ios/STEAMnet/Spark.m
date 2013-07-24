//
//  Spark.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/16/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "Spark.h"
#import "Comment.h"
#import "Idea.h"
#import "Tag.h"
#import "User.h"

@implementation Spark

@dynamic content;
@dynamic contentType;
@dynamic sparkType;
@dynamic fileURL;
@dynamic ideas;
@dynamic users;

@synthesize cachedImage;

+ (UIColor *)colorForSparkType:(NSString *)sparkType
{
    UIColor *color;
    
    float base = 87.0/255.0;
    float max = 237.0/255.0;
    
    if ([sparkType isEqual:@"I"]) {
        color = [UIColor colorWithRed:base green:base blue:max alpha:1.0];
    } else if ([sparkType isEqual:@"P"]) {
        color = [UIColor colorWithRed:max green:base blue:base alpha:1.0];
    } else if ([sparkType isEqual:@"W"]) {
        color = [UIColor colorWithRed:base green:max blue:base alpha:1.0];
    }
    
    return color;
}

+ (UIColor *)colorForContentType:(NSString *)contentType
{
    return [UIColor yellowColor];
}

@end
