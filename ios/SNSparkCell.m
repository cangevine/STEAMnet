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
@property (nonatomic, strong) UIImageView *imageView;

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
        
        self.imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        [self addSubview:self.imageView];
    }
    return self;
}

- (void)setSpark:(Spark *)aSpark
{
    spark = aSpark;
    
    self.info.text = [NSString stringWithFormat:@"%@", spark.remoteId];
    
    if (([spark.contentType  isEqual: @"P"]) || ([spark.contentType  isEqual: @"C"]) || ([spark.contentType  isEqual: @"V"]) || ([spark.contentType  isEqual: @"L"])) {
        NSURL *url = [NSURL URLWithString:spark.fileURL];
        NSData *data = [NSData dataWithContentsOfURL:url];
        UIImage *img = [[UIImage alloc] initWithData:data];
        self.imageView.image = img;
    }
    NSLog(@"file: %@", spark.fileURL);
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
