//
//  SNIdeaCell.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/15/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "SNIdeaCell.h"

#import "Idea.h"

@interface SNIdeaCell ()

@property (nonatomic, strong) UILabel *info;

@end

@implementation SNIdeaCell

@synthesize idea;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor orangeColor];
        self.info = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, 100, 100)];
        [self addSubview:self.info];
    }
    return self;
}

- (void)setIdea:(Idea *)anIdea
{
    idea = anIdea;
    
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"MMMM d, YYYY"];
    
    self.info.text = [dateFormat stringFromDate:idea.createdDate];
//    self.info.text = [NSString stringWithFormat:@"%@", idea.remoteId];
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
