//
//  SNNewSparkViewController.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/22/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "SNNewSparkViewController.h"

#import "Spark.h"

#import <QuartzCore/QuartzCore.h>

@interface SNNewSparkViewController ()

@property (strong, nonatomic) IBOutlet UIView *sparkTypeView;
@property (strong, nonatomic) IBOutlet UIView *contentTypeView;
@property (strong, nonatomic) IBOutlet UIView *contentView;
@property (strong, nonatomic) IBOutlet UIButton *problemButton;
@property (strong, nonatomic) IBOutlet UIButton *inspirationButton;
@property (strong, nonatomic) IBOutlet UIButton *questionButton;

@property (strong, nonatomic) IBOutlet UIButton *audioButton;
@property (strong, nonatomic) IBOutlet UIButton *codeButton;
@property (strong, nonatomic) IBOutlet UIButton *linkButton;
@property (strong, nonatomic) IBOutlet UIButton *pictureButton;
@property (strong, nonatomic) IBOutlet UIButton *textButton;
@property (strong, nonatomic) IBOutlet UIButton *videoButton;

@property (strong, nonatomic) NSArray *sparkTypeButtons;
@property (strong, nonatomic) NSArray *contentTypeButtons;

- (void)setBackgroundColorOnButton:(UIButton *)button;

@end

@implementation SNNewSparkViewController

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        
//        NSLog(@"layout");
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.title = @"New Spark";
    self.view.backgroundColor = [UIColor blackColor];
    
    NSLayoutConstraint *widthConstraint = [NSLayoutConstraint constraintWithItem:self.inspirationButton attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:self.sparkTypeView attribute:NSLayoutAttributeWidth multiplier:0.333333333 constant:0];
    [self.sparkTypeView addConstraint:widthConstraint];
    
    self.sparkTypeButtons = @[self.inspirationButton, self.problemButton, self.questionButton];
    
    for (UIButton *b in self.sparkTypeButtons) {
        [self setBackgroundColorOnButton:b];
    }
    
    self.contentTypeButtons = @[self.audioButton, self.codeButton, self.linkButton, self.pictureButton, self.textButton, self.videoButton];
    
    for (UIButton *b in self.contentTypeButtons) {
        NSLayoutConstraint *otherWidthConstraint = [NSLayoutConstraint constraintWithItem:b attribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:self.contentTypeView attribute:NSLayoutAttributeWidth multiplier:0.166666667 constant:0];
        [self.contentTypeView addConstraint:otherWidthConstraint];
        
        [self setBackgroundColorOnButton:b];
    }
    
    self.contentTypeView.transform = CGAffineTransformMakeTranslation(0, -2 * self.contentTypeView.frame.size.height);
    self.contentTypeView.alpha = 0.0;
    
    self.contentView.alpha = 0.0;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setBackgroundColorOnButton:(UIButton *)button
{
    if ([button isEqual:self.inspirationButton]) {
//        button.backgroundColor = [UIColor colorWithRed:87.0/255.0 green:87.0/255.0 blue:237.0/255.0 alpha:1.0];
        button.backgroundColor = [Spark colorForSparkType:@"I"];
    } else if ([button isEqual:self.problemButton]) {
//        button.backgroundColor = [UIColor colorWithRed:237.0/255.0 green:87.0/255.0 blue:87.0/255.0 alpha:1.0];
        button.backgroundColor = [Spark colorForSparkType:@"P"];
    } else if ([button isEqual:self.questionButton]) {
//        button.backgroundColor = [UIColor colorWithRed:87.0/255.0 green:237.0/255.0 blue:87.0/255.0 alpha:1.0];
        button.backgroundColor = [Spark colorForSparkType:@"W"];
    } else if ([self.contentTypeButtons containsObject:button]) {
        button.backgroundColor = [Spark colorForContentType:button.titleLabel.text];
    }
}

- (IBAction)cancelButtonPressed:(id)sender {
    [self.parentViewController dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)sparkTypeButtonPressed:(UIButton *)sender {
    [UIView animateWithDuration:0.25 animations:^(void) {
        for (UIButton *button in self.sparkTypeButtons) {
            if (button != sender) {
                button.backgroundColor = [UIColor darkGrayColor];
            } else {
                [self setBackgroundColorOnButton:button];
            }
        }
        
        self.contentTypeView.alpha = 1.0;
        self.contentTypeView.transform = CGAffineTransformMakeTranslation(0, -self.contentTypeView.frame.size.height);
    } completion:^(BOOL finished) {
    
    }];
}

@end
