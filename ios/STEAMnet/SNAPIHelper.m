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
    
    NSMutableString *after = [[NSMutableString alloc] init];
    
    [after appendString:(NSString *)[[NSUserDefaults standardUserDefaults] objectForKey:@"token"]];
    
    if (options) {
        for (NSString *key in options) {
            NSString *value = (NSString *)[options valueForKey:key];
            NSString *optionString = [[[@"&" stringByAppendingString:key] stringByAppendingString:@"="] stringByAppendingString:value];
            
            [after appendString:optionString];
        }
    }
    
    NSString *urlString = [[[base stringByAppendingString:path] stringByAppendingString:@".json?token="] stringByAppendingString:after];
    
    return [NSURL URLWithString:urlString];
}

@end
