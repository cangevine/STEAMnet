//
//  SNAPIHelper.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/18/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "SNAPIHelper.h"

@implementation SNAPIHelper

+ (NSURL *)urlForPath:(NSString *)path options:(NSDictionary *)options
{
    NSString *base = @"http://steamnet.herokuapp.com/api/v1/";
    
    NSString *token = (NSString *)[[NSUserDefaults standardUserDefaults] objectForKey:@"token"];
    
    NSMutableString *optionsString = [[NSMutableString alloc] init];
    
    if (options) {
        for (NSString *key in options) {
            NSString *value = (NSString *)[options valueForKey:key];
            NSString *optionString = [[[@"&" stringByAppendingString:key] stringByAppendingString:@"="] stringByAppendingString:value];
            
            [optionsString appendString:optionString];
        }
    }
    
    NSString *urlString = [[[[base stringByAppendingString:path] stringByAppendingString:@".json?token="] stringByAppendingString:token] stringByAppendingString:optionsString];
    
    return [NSURL URLWithString:urlString];
}

@end
