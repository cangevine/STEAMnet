//
//  SNLoginViewController.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/15/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol SNLoginViewControllerDelegate <NSObject>

- (void)didLogIn:(BOOL)loggedIn;

@end

@interface SNLoginViewController : UIViewController <UIWebViewDelegate>

@property (nonatomic, strong) id<SNLoginViewControllerDelegate> delegate;

@end
