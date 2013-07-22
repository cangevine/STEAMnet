//
//  SNAPIHelper.h
//  STEAMnet
//
//  Created by Max Luzuriaga on 7/18/13.
//  Copyright (c) 2013 Max Luzuriaga. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SNAPIHelper : NSObject

+ (NSURL *)urlForPath:(NSString *)path options:(NSDictionary *)options;

@end
