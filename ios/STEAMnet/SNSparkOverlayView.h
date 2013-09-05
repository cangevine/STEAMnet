//
//  SNSparkOverlayView.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/22/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SNSparkOverlayView : UIView

@property (nonatomic, strong) NSString *sparkType;
@property (nonatomic, strong, readonly) UIColor *color;

@end
