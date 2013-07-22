//
//  SNSparkOverlayView.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/22/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "SNSparkOverlayView.h"

@implementation SNSparkOverlayView

@synthesize color;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
    }
    return self;
}

- (void)setSparkType:(NSString *)theSparkType
{
    _sparkType = theSparkType;
    
    if ([_sparkType isEqual:@"I"]) {
        color = [UIColor colorWithRed:87.0/255.0 green:87.0/255.0 blue:237.0/255.0 alpha:1.0];
    } else if ([_sparkType isEqual:@"P"]) {
        color = [UIColor colorWithRed:237.0/255.0 green:87.0/255.0 blue:87.0/255.0 alpha:1.0];
    } else if ([_sparkType isEqual:@"W"]) {
        color = [UIColor colorWithRed:87.0/255.0 green:237.0/255.0 blue:87.0/255.0 alpha:1.0];
    }
}

- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetLineCap(context, kCGLineCapRound);
    CGContextSetStrokeColorWithColor(context, [[color colorWithAlphaComponent:0.9] CGColor]);
    CGContextSetLineWidth(context, 20.0);
    
    CGContextAddRect(context, rect);
    
    CGContextStrokePath(context);
}

@end
