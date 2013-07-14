# STEAMnet API #

## V1 ##

The STEAMnet API version 1 can be accessed at `http://steamnet.herokuapp.com/api/v1/`, so throughout this README any urls that I reference will be an extension of this base url. For instance, if I talk about a GET request to `/users.json`, what I really mean is a GET request to `http://steamnet.herokuapp.com/api/v1/users.json`.

From now on, every request to the STEAMnet API must be authenticated with a Token, which is generated when a user logs in or creates an account from the Android app (a detailed spec of how this should work in the App can be found in the description for [Pull #62](https://github.com/cangevine/STEAMnet/pull/62)). Once the Token is saved to the Android device, every single request to the api must include the token in the request url. For instance, that `/users.json` request I mentioned previously would not work unless the token is included like so: `http://steamnet.herokuapp.com/api/v1/users.json?token=yourdevicetoken`. The Tokens are associated with a given user and Device, so that the API is aware from which User the request comes, and so that it's aware that the request is coming from the STEAMnet app, and not some malicious third party.

*(Note 7/14/13: currently the API does not use the token authentication, because none of the Android code has been updated to use it yet, but once the Android app is updated to generate and use these tokens, the switch will be flipped and Token authentication will be required for every request.)*

### Sparks ###

#### Get All Sparks ####

To get a list of all the sparks in the database, issue a GET request to `/sparks.json?limit=x`, where `x` is the number of sparks you'd like to fetch. If no limit is specified, every spark in the database will be returned. Either way, sparks will be returned in descending order, with the most recent sparks first, going down all the way to the very first spark.

A sample return of a request to `/sparks.json?limit=2` can be seen here:

	[
		{
			"id":18,
			"created_at":"2013-07-13T16:29:00.615Z",
			"spark_type":"P",
			"content_type":"V",
			"content":"http://www.youtube.com/watch?v=j5C6X9vOEkU",
			"content_hash":"a83cf9063c28da791497a752a2657f5881d30b3a",
			"users":[
				{
					"id":6,
					"name":"Dan Stadtmauer",
					"email":"rocio@feeneyconsidine.biz"
				},
				{
					"id":8,
					"name":"Grace Heard",
					"email":"pauline@morar.net"
				}
			],
			"ideas":[
				{
					"id":2,
					"created_at":"2013-07-13T16:29:00.694Z",
					"description":"Rerum adipisci vel quod et incidunt excepturi suscipit."
				},
				{
					"id":10,
					"created_at":"2013-07-13T16:29:00.868Z",
					"description":"Et quo rerum laboriosam qui saepe fugit minus quasi."
				},
				{
					"id":14,
					"created_at":"2013-07-13T16:29:01.011Z",
					"description":"Dolores nostrum est dolorum exercitationem."
				}
			],
			"comments":[
				{
					"id":27,
					"comment_text":"Quaerat facere enim error architecto. Id eligendi est praesentium qui labore ipsam nisi. Aliquam praesentium perspiciatis quia velit veniam.",
					"created_at":"2013-07-13T16:29:00.634Z",
					"user":{
						"id":1,
						"name":"Max",
						"email":"bryon.ryan@fisher.com"
					}
				},
				{
					"id":28,
					"comment_text":"Et culpa officia. Quam voluptatem aut iste. Eum et sunt odio.",
					"created_at":"2013-07-13T16:29:00.637Z",
					"user":{
						"id":1,
						"name":"Max",
						"email":"bryon.ryan@fisher.com"
					}
				}
			],
			"tags":[
				"yellow",
				"rails",
				"purple"
			]
		},
		{
			"id":17,
			"created_at":"2013-07-13T16:29:00.615Z",
			"spark_type":"P",
			"content_type":"V",
			"content":"http://www.youtube.com/watch?v=j5C6X9vOEkU",
			"content_hash":"a83cf9063c28da791497a752a2657f5881d30b3a",
			"users":[
				{
					"id":6,
					"name":"Dan Stadtmauer",
					"email":"rocio@feeneyconsidine.biz"
				},
				{
					"id":8,
					"name":"Grace Heard",
					"email":"pauline@morar.net"
				}
			],
			"ideas":[
				{
					"id":2,
					"created_at":"2013-07-13T16:29:00.694Z",
					"description":"Rerum adipisci vel quod et incidunt excepturi suscipit."
				},
				{
					"id":10,
					"created_at":"2013-07-13T16:29:00.868Z",
					"description":"Et quo rerum laboriosam qui saepe fugit minus quasi."
				},
				{
					"id":14,
					"created_at":"2013-07-13T16:29:01.011Z",
					"description":"Dolores nostrum est dolorum exercitationem."
				}
			],
			"comments":[
				{
					"id":27,
					"comment_text":"Quaerat facere enim error architecto. Id eligendi est praesentium qui labore ipsam nisi. Aliquam praesentium perspiciatis quia velit veniam.",
					"created_at":"2013-07-13T16:29:00.634Z",
					"user":{
						"id":1,
						"name":"Max",
						"email":"bryon.ryan@fisher.com"
					}
				},
				{
					"id":28,
					"comment_text":"Et culpa officia. Quam voluptatem aut iste. Eum et sunt odio.",
					"created_at":"2013-07-13T16:29:00.637Z",
					"user":{
						"id":1,
						"name":"Max",
						"email":"bryon.ryan@fisher.com"
					}
				}
			],
			"tags":[
				"yellow",
				"rails",
				"purple"
			]
		},
	]

#### Get One spark ####

Similarly, to get all the details about just one spark, issue a request to `/sparks/id.json`, where id is the id of the spark you want to get. A sample response to `/sparks/18.json` for instance, is strikingly similar to the response for getting all sparks, but with just one:

	{
		"id":18,
		"created_at":"2013-07-13T16:29:00.615Z",
		"spark_type":"P",
		"content_type":"V",
		"content":"http://www.youtube.com/watch?v=j5C6X9vOEkU",
		"content_hash":"a83cf9063c28da791497a752a2657f5881d30b3a",
		"users":[
			{
				"id":6,
				"name":"Dan Stadtmauer",
				"email":"rocio@feeneyconsidine.biz"
			},
			{
				"id":8,
				"name":"Grace Heard",
				"email":"pauline@morar.net"
			}
		],
		"ideas":[
			{
				"id":2,
				"created_at":"2013-07-13T16:29:00.694Z",
				"description":"Rerum adipisci vel quod et incidunt excepturi suscipit."
			},
			{
				"id":10,
				"created_at":"2013-07-13T16:29:00.868Z",
				"description":"Et quo rerum laboriosam qui saepe fugit minus quasi."
			},
			{
				"id":14,
				"created_at":"2013-07-13T16:29:01.011Z",
				"description":"Dolores nostrum est dolorum exercitationem."
			}
		],
		"comments":[
			{
				"id":27,
				"comment_text":"Quaerat facere enim error architecto. Id eligendi est praesentium qui labore ipsam nisi. Aliquam praesentium perspiciatis quia velit veniam.",
				"created_at":"2013-07-13T16:29:00.634Z",
				"user":{
					"id":1,
					"name":"Max",
					"email":"bryon.ryan@fisher.com"
				}
			},
			{
				"id":28,
				"comment_text":"Et culpa officia. Quam voluptatem aut iste. Eum et sunt odio.",
				"created_at":"2013-07-13T16:29:00.637Z",
				"user":{
					"id":1,
					"name":"Max",
					"email":"bryon.ryan@fisher.com"
				}
			}
		],
		"tags":[
			"yellow",
			"rails",
			"purple"
		]
	}

#### Creating a Spark ####

To create a spark, issue a POST request to `/sparks.json`. The available options are as follows

* `spark[spark_type]`
	* Either "W" for what-if question, "I" for inspiration, or "P" for problem. No other characters will be accepted.
* `spark[content_type]`
	* "L" for link, "V" for video, "C" for code, "T" for text, "P" for picture, and "A" for audio. Again, only these specific characters will be allowed.
* `spark[content]`
	* unless the spark is a code or text spark, this should be a url string. For links, it could be a link to any website, for videos a link to youtube, pictures a link to a picture file (support for file uploading coming later), and audio a link to a soundcloud page.
* `tags`
	* A comma-separated list of strings to represent the tags, with no spaces. Tags can only be alphanumerical, plus hyphens and underscores For example "red,green,blue,some-tag,the_tag"

The API will determine which User is creating the spark based on the Token provided (for now this is done with the `username` attribute, but this will be deprecated once I turn on Token authentication). If the spark exists already, the API will add this user to the spark. If it doesn't exist, the API will create the spark and add the user to it. You can tell which scenario has occured based on the `spark_is_new` attribute of the JSON response, as seen in the response below.

A POST request to `/sparks.json?spark[spark_type]=I&spark[content_type]=T&spark[content]=Some%20Text&tags=red,green,blue`, when no spark with that content and content type exists in the database already, will respond with:

	{
		"spark_is_new":true, // Only present if the spark was just created.
		"id":19,
		"created_at":"2013-07-13T16:29:00.615Z",
		"spark_type":"I",
		"content_type":"T",
		"content":"Some Text",
		"content_hash":"a83cf9063c28da791497a752a2657f5881d30b3a",
		"users":[
			{
				"id":6,
				"name":"Dan Stadtmauer",
				"email":"rocio@feeneyconsidine.biz"
			},
		],
		"ideas":[],
		"comments":[],
		"tags":[
			"red",
			"green",
			"blue"
		]
	}
