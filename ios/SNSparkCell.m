//
//  SNSparkCell.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/15/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "SNSparkCell.h"

#import "Spark.h"

@interface SNSparkCell ()

@property (nonatomic, strong) UILabel *info;

@end

@implementation SNSparkCell

@synthesize spark;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor purpleColor];
        self.info = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, 100, 100)];
        [self addSubview:self.info];
    }
    return self;
}

- (void)setSpark:(Spark *)aSpark
{
    spark = aSpark;
    
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"MMMM d, YYYY"];
    
    self.info.text = [dateFormat stringFromDate:spark.createdDate];
//    self.info.text = [NSString stringWithFormat:@"%@", spark.remoteId];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
