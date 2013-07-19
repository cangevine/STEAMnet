//
//  NSDate+RailsDates.m
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/18/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import "NSDate+RailsDates.h"

@implementation NSDate (RailsDates)

+ (NSDate *)dateWithISO8601String:(NSString *)dateString
{
    if (!dateString) return nil;
    if ([dateString hasSuffix:@"Z"]) {
        dateString = [[dateString substringToIndex:(dateString.length-1)] stringByAppendingString:@"-0000"];
    }
    
    return [self dateFromString:dateString withFormat:@"yyyy-MM-dd'T'HH:mm:ssZ"];
}

+ (NSDate *)dateFromString:(NSString *)dateString withFormat:(NSString *)dateFormat
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:dateFormat];
    
    NSLocale *locale = [[NSLocale alloc]
                        initWithLocaleIdentifier:@"en_US_POSIX"];
    [dateFormatter setLocale:locale];
    
    NSDate *date = [dateFormatter dateFromString:dateString];
    return date;
}

@end
