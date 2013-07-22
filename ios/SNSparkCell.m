//
//  SNSparkCell.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/15/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "SNSparkCell.h"

#import "Spark.h"
#import "SNSparkOverlayView.h"

#import "UIImage+ImageEffects.h"
#import <QuartzCore/QuartzCore.h>

@interface SNSparkCell ()

@property (nonatomic, strong) UILabel *info;
@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) SNSparkOverlayView *overlayView;

- (void)downloadImageWithURL:(NSURL *)url completion:(void (^)(BOOL succeeded, UIImage *image))completionBlock;

@end

@implementation SNSparkCell

@synthesize spark;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.info = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, 100, 100)];
        [self.contentView addSubview:self.info];
        
        self.imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.contentView.frame.size.width, self.contentView.frame.size.height)];
        self.imageView.contentMode = UIViewContentModeScaleAspectFill;
        self.imageView.clipsToBounds = YES;
        [self.contentView addSubview:self.imageView];
        
        self.overlayView = [[SNSparkOverlayView alloc] initWithFrame:self.contentView.frame];
        [self addSubview:self.overlayView];
    }
    return self;
}

- (void)setSpark:(Spark *)aSpark
{
    spark = aSpark;
    
    self.overlayView.sparkType = spark.sparkType;
    self.backgroundColor = self.overlayView.color;
    
    self.info.text = [NSString stringWithFormat:@"%@", spark.remoteId];
    
    if (([spark.contentType  isEqual: @"P"]) || ([spark.contentType  isEqual: @"C"]) || ([spark.contentType  isEqual: @"V"]) || ([spark.contentType  isEqual: @"L"])) {
        if (spark.cachedImage) {
            self.imageView.image = spark.cachedImage;
        } else {
            NSURL *url = [NSURL URLWithString:spark.fileURL];
            
            [self downloadImageWithURL:url completion:^(BOOL succeeded, UIImage *image) {
                if (succeeded) {
                    self.imageView.image = image;
                    
                    spark.cachedImage = image;
                }
            }];
        }
    }
}

- (void)downloadImageWithURL:(NSURL *)url completion:(void (^)(BOOL succeeded, UIImage *image))completionBlock
{
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    [NSURLConnection sendAsynchronousRequest:request queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
       if (!error) {
           UIImage *image = [[UIImage alloc] initWithData:data];
           completionBlock(YES, image);
       } else {
           completionBlock(NO, nil);
       }
    }];
}

@end
