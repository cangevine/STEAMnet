//
//  SNSparkOverlayView.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/22/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "SNSparkOverlayView.h"

#import "Spark.h"

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
    
    color = [Spark colorForSparkType:_sparkType];
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
