# STEAMnet API

## V1

The STEAMnet API version 1 can be accessed at `http://steamnet.herokuapp.com/api/v1/`, so throughout this README any urls that I reference will be an extension of this base url. For instance, if I talk about a GET request to `/users.json`, what I really mean is a GET request to `http://steamnet.herokuapp.com/api/v1/users.json`.

From now on, every request to the STEAMnet API must be authenticated with a Token, which is generated when a user logs in or creates an account from the Android app (a detailed spec of how this should work in the App can be found in the description for [Pull #62](https://github.com/cangevine/STEAMnet/pull/62)). Once the Token is saved to the Android device, every single request to the api must include the token in the request url. For instance, that `/users.json` request I mentioned previously would not work unless the token is included like so: `http://steamnet.herokuapp.com/api/v1/users.json?token=yourdevicetoken`. The Tokens are associated with a given user and Device, so that the API is aware from which User the request comes, and so that it's aware that the request is coming from the STEAMnet app, and not some malicious third party.

If a request _is_ issued to the API without a token, or with a token that doesn't correspond to a user, the API will respond with no response body, and a response code of 401 unauthorized.

*(Note 7/14/13: currently the API does not use the token authentication, because none of the Android code has been updated to use it yet, but once the Android app is updated to generate and use these tokens, the switch will be flipped and Token authentication will be required for every request.)*

### Sparks

#### Get All Sparks

To get a list of all the sparks in the database, issue a GET request to `/sparks.json?limit=x`, where `x` is the number of sparks you'd like to fetch. If no limit is specified, every spark in the database will be returned. Either way, sparks will be returned in descending order, with the most recent sparks first, going down all the way to the very first spark.

A sample return of a request to `/sparks.json?limit=2` can be seen here:

	[
		{
			"id" : 18,
			"created_at" : "2013-07-13T16:29:00.615Z",
			"spark_type" : "P",
			"content_type" : "V",
			"content" : "http://www.youtube.com/watch?v=j5C6X9vOEkU",
			"content_hash" : "a83cf9063c28da791497a752a2657f5881d30b3a",
			"users" : [
				{
					"id" : 6,
					"name" : "Dan Stadtmauer",
					"email" : "rocio@feeneyconsidine.biz"
				},
				{
					"id" : 8,
					"name" : "Grace Heard",
					"email" : "pauline@morar.net"
				}
			],
			"ideas" : [
				{
					"id" : 2,
					"created_at" : "2013-07-13T16:29:00.694Z",
					"description" : "Rerum adipisci vel quod et incidunt excepturi suscipit."
				},
				{
					"id" : 10,
					"created_at" : "2013-07-13T16:29:00.868Z",
					"description" : "Et quo rerum laboriosam qui saepe fugit minus quasi."
				},
				{
					"id" : 14,
					"created_at" : "2013-07-13T16:29:01.011Z",
					"description" : "Dolores nostrum est dolorum exercitationem."
				}
			],
			"comments" : [
				{
					"id" : 27,
					"comment_text" : "Quaerat facere enim error architecto. Id eligendi est praesentium qui labore ipsam nisi. Aliquam praesentium perspiciatis quia velit veniam.",
					"created_at" : "2013-07-13T16:29:00.634Z",
					"user" : {
						"id" : 1,
						"name" : "Max",
						"email" : "bryon.ryan@fisher.com"
					}
				},
				{
					"id" : 28,
					"comment_text" : "Et culpa officia. Quam voluptatem aut iste. Eum et sunt odio.",
					"created_at" : "2013-07-13T16:29:00.637Z",
					"user" : {
						"id" : 1,
						"name" : "Max",
						"email" : "bryon.ryan@fisher.com"
					}
				}
			],
			"tags" : [
				"yellow",
				"rails",
				"purple"
			]
		},
		{
			"id" : 17,
			"created_at" : "2013-07-13T16:29:00.615Z",
			"spark_type" : "P",
			"content_type" : "V",
			"content" : "http://www.youtube.com/watch?v=j5C6X9vOEkU",
			"content_hash" : "a83cf9063c28da791497a752a2657f5881d30b3a",
			"users" : [
				{
					"id" : 6,
					"name" : "Dan Stadtmauer",
					"email" : "rocio@feeneyconsidine.biz"
				},
				{
					"id" : 8,
					"name" : "Grace Heard",
					"email" : "pauline@morar.net"
				}
			],
			"ideas" : [
				{
					"id" : 2,
					"created_at" : "2013-07-13T16:29:00.694Z",
					"description" : "Rerum adipisci vel quod et incidunt excepturi suscipit."
				},
				{
					"id" : 10,
					"created_at" : "2013-07-13T16:29:00.868Z",
					"description" : "Et quo rerum laboriosam qui saepe fugit minus quasi."
				},
				{
					"id" : 14,
					"created_at" : "2013-07-13T16:29:01.011Z",
					"description" : "Dolores nostrum est dolorum exercitationem."
				}
			],
			"comments" : [
				{
					"id" : 27,
					"comment_text" : "Quaerat facere enim error architecto. Id eligendi est praesentium qui labore ipsam nisi. Aliquam praesentium perspiciatis quia velit veniam.",
					"created_at" : "2013-07-13T16:29:00.634Z",
					"user" : {
						"id" : 1,
						"name" : "Max",
						"email" : "bryon.ryan@fisher.com"
					}
				},
				{
					"id" : 28,
					"comment_text" : "Et culpa officia. Quam voluptatem aut iste. Eum et sunt odio.",
					"created_at" : "2013-07-13T16:29:00.637Z",
					"user" : {
						"id" : 1,
						"name" : "Max",
						"email" : "bryon.ryan@fisher.com"
					}
				}
			],
			"tags" : [
				"yellow",
				"rails",
				"purple"
			]
		},
	]

#### Get One Spark

Similarly, to get all the details about just one spark, issue a request to `/sparks/id.json`, where id is the id of the spark you want to get. A sample response to `/sparks/18.json` for instance, is strikingly similar to the response for getting all sparks, but with just one:

	{
		"id" : 18,
		"created_at" : "2013-07-13T16:29:00.615Z",
		"spark_type" : "P",
		"content_type" : "V",
		"content" : "http://www.youtube.com/watch?v=j5C6X9vOEkU",
		"content_hash" : "a83cf9063c28da791497a752a2657f5881d30b3a",
		"users" : [
			{
				"id" : 6,
				"name" : "Dan Stadtmauer",
				"email" : "rocio@feeneyconsidine.biz"
			},
			{
				"id" : 8,
				"name" : "Grace Heard",
				"email" : "pauline@morar.net"
			}
		],
		"ideas" : [
			{
				"id" : 2,
				"created_at" : "2013-07-13T16:29:00.694Z",
				"description" : "Rerum adipisci vel quod et incidunt excepturi suscipit."
			},
			{
				"id" : 10,
				"created_at" : "2013-07-13T16:29:00.868Z",
				"description" : "Et quo rerum laboriosam qui saepe fugit minus quasi."
			},
			{
				"id" : 14,
				"created_at" : "2013-07-13T16:29:01.011Z",
				"description" : "Dolores nostrum est dolorum exercitationem."
			}
		],
		"comments" : [
			{
				"id" : 27,
				"comment_text" : "Quaerat facere enim error architecto. Id eligendi est praesentium qui labore ipsam nisi. Aliquam praesentium perspiciatis quia velit veniam.",
				"created_at" : "2013-07-13T16:29:00.634Z",
				"user" : {
					"id" : 1,
					"name" : "Max",
					"email" : "bryon.ryan@fisher.com"
				}
			},
			{
				"id" : 28,
				"comment_text" : "Et culpa officia. Quam voluptatem aut iste. Eum et sunt odio.",
				"created_at" : "2013-07-13T16:29:00.637Z",
				"user" : {
					"id" : 1,
					"name" : "Max",
					"email" : "bryon.ryan@fisher.com"
				}
			}
		],
		"tags" : [
			"yellow",
			"rails",
			"purple"
		]
	}

#### Creating a Spark

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
		"spark_is_new" : true, // Only present if the spark was just created.
		"id" : 19,
		"created_at" : "2013-07-13T16:29:00.615Z",
		"spark_type" : "I",
		"content_type" : "T",
		"content" : "Some Text",
		"content_hash" : "a83cf9063c28da791497a752a2657f5881d30b3a",
		"users" : [
			{
				"id" : 6,
				"name" : "Dan Stadtmauer",
				"email" : "rocio@feeneyconsidine.biz"
			},
		],
		"ideas" : [],
		"comments" : [],
		"tags" : [
			"red",
			"green",
			"blue"
		]
	}

#### Deleting a Spark

Sparks are never deleted in STEAMnet. Instead, the idea of "deleting" a spark from a certain user account only means breaking the connection between that particular user and the spark. This way, Sparks are preserved even if a User deletes their account, or decides not to take ownership of that spark anymore.

To break this relationship, issue a DELETE request to `/sparks/id.json`. To do a delete request in Android, call `.setRequestMethod("DELETE")` on your HttpURLConnection before running it. The API will detect which user is making the request using the Token, and remove that user from the spark. The response for this request is the same as that of showing or creating a spark, except with the proper user removed from the Spark's "users" array.

### Ideas

#### Get All Ideas

To get an array of ideas in the database, issue a GET request to `/ideas.json?limit=x`, where `x` is the number of ideas you'd like to fetch. If no limit is specified, all the ideas will be fetched. Just like this request for sparks, all the ideas will be returned in descending order, with the newest ideas first.

A sample output of a request to `/ideas.json?limit=2` can be seen here:
	
	[
		{
			"id" : 28,
			"created_at" : "2013-07-11T23:10:09.090Z",
			"description" : "MyDescriptionTest",
			"user" : {
				"id" : 1,
				"name" : "max",
				"email" : "bethany@weimann.org"
			},
			"sparks" : [
				{
					"id" : 1,
					"created_at" : "2013-06-26T23:52:02.720Z",
					"spark_type" : "W",
					"content_type" : "L",
					"content" : "http://watsica.name/mark",
					"content_hash" : "f3aee28e900cf5daf719022eb6ce07d48f22b8a3"
				},
				{
					"id" : 4,
					"created_at" : "2013-06-26T23:52:03.789Z",
					"spark_type" : "W",
					"content_type" : "C",
					"content" : "User.create!(:name =\u003E octavia.kiehn)",
					"content_hash" : "4486f258a06046f485808592fb4e0c4ba7404ce6"
				},
				{
					"id" : 6,
					"created_at" : "2013-06-26T23:52:04.054Z",
					"spark_type" : "P",
					"content_type" : "C",
					"content" : "User.create!(:name =\u003E casimir)",
					"content_hash" : "c0638938e7c205f7ad084405d9e61d0518ae0e4a"
				}
			],
			"comments" : [],
			"tags" : [
				"purple",
				"awesome"
			]
		},
		{
			"id" : 27,
			"created_at" : "2013-07-11T06:58:50.190Z",
			"description" : "Bed Time",
			"user" : {
				"id" : 1,
				"name" : "max",
				"email" : "bethany@weimann.org"
			},
			"sparks" : [
				{
					"id" : 46,
					"created_at" : "2013-07-10T21:44:43.637Z",
					"spark_type" : "I",
					"content_type" : "T",
					"content" : "Test with new wizard",
					"content_hash" : "7f97948351c659e07e0841280689c2e32dc46850"
				},
				{
					"id" : 48,
					"created_at" : "2013-07-11T06:36:35.091Z",
					"spark_type" : "I",
					"content_type" : "T",
					"content" : "I lied...",
					"content_hash" : "37b3a48cedc2c1c29554f6ab3f4fef89c8e7072f"
				},{
					"id" : 49,
					"created_at" : "2013-07-11T06:44:22.556Z",
					"spark_type" : "I",
					"content_type" : "T",
					"content" : "I'm an idiot",
					"content_hash" : "0581eea28d2ff4c8618057c2b2ed8a2bd8348f44"
				},{
					"id" : 44,
					"created_at" : "2013-07-05T18:54:50.228Z",
					"spark_type" : "P",
					"content_type" : "T",
					"content" : "LALALALA IM A TEST",
					"content_hash" : "cded8b8809896c6784b620fe6de088dde0b234a1"
				}
			],
			"comments" : [],
			"tags" : [
				"bugged",
				"crashes"
			]
		}
	]

#### Get One Idea ####

To get all these above attributes, but only for one idea, do the same as you would for one spark: `/sparks/id.json`. The response, dear reader, would be as follows:

	{
		"id" : 28,
		"created_at" : "2013-07-11T23:10:09.090Z",
		"description" : "MyDescriptionTest",
		"user" : {
			"id" : 1,
			"name" : "max",
			"email" : "bethany@weimann.org"
		},
		"sparks" : [
			{
				"id" : 1,
				"created_at" : "2013-06-26T23:52:02.720Z",
				"spark_type" : "W",
				"content_type" : "L",
				"content" : "http://watsica.name/mark",
				"content_hash" : "f3aee28e900cf5daf719022eb6ce07d48f22b8a3"
			},
			{
				"id" : 4,
				"created_at" : "2013-06-26T23:52:03.789Z",
				"spark_type" : "W",
				"content_type" : "C",
				"content" : "User.create!(:name =\u003E octavia.kiehn)",
				"content_hash" : "4486f258a06046f485808592fb4e0c4ba7404ce6"
			},
			{
				"id" : 6,
				"created_at" : "2013-06-26T23:52:04.054Z",
				"spark_type" : "P",
				"content_type" : "C",
				"content" : "User.create!(:name =\u003E casimir)",
				"content_hash" : "c0638938e7c205f7ad084405d9e61d0518ae0e4a"
			}
		],
		"comments" : [],
		"tags" : [
			"purple",
			"awesome"
		]
	}

#### Creating Ideas

To create a spark, issue a POST request to `/sparks.json`. The available options are as follows

* `idea[description]`
	* The description you want your created idea to have
* `sparks`
	* A comma-separated list of spark ids to represent each spark out of which you want to make the idea. For instance, this might be something like "1,5,7,24"
* `tags`
	* A comma-separated list of strings to represent the tags, with no spaces. Tags can only be alphanumerical, plus hyphens and underscores For example "red,green,blue,some-tag,the_tag"

The API will determine which User is creating the idea based on the Token provided (for now this is done with the `username` attribute, but this will be deprecated once I turn on Token authentication), and it will set the Idea's user to be this user.

A POST request to `/ideas.json?idea[description]=Some%20Description&sparks=1,5,7&tags=red,green,blue`, will respond with the json representation of the newly created idea:

	{
		"id" : 28,
		"created_at" : "2013-07-11T23:10:09.090Z",
		"description" : "Some Description",
		"user" : {
			"id" : 1,
			"name" : "max",
			"email" : "bethany@weimann.org"
		},
		"sparks" : [
			{
				"id" : 1,
				"created_at" : "2013-06-26T23:52:02.720Z",
				"spark_type" : "W",
				"content_type" : "L",
				"content" : "http://watsica.name/mark",
				"content_hash" : "f3aee28e900cf5daf719022eb6ce07d48f22b8a3"
			},
			{
				"id" : 5,
				"created_at" : "2013-06-26T23:52:03.789Z",
				"spark_type" : "W",
				"content_type" : "C",
				"content" : "User.create!(:name =\u003E octavia.kiehn)",
				"content_hash" : "4486f258a06046f485808592fb4e0c4ba7404ce6"
			},
			{
				"id" : 7,
				"created_at" : "2013-06-26T23:52:04.054Z",
				"spark_type" : "P",
				"content_type" : "C",
				"content" : "User.create!(:name =\u003E casimir)",
				"content_hash" : "c0638938e7c205f7ad084405d9e61d0518ae0e4a"
			}
		],
		"comments" : [],
		"tags" : [
			"red",
			"green",
			"blue"
		]
	}
	
#### Deleting an Idea

Just like sparks, ideas are never deleted in STEAMnet. Instead, when the owner of an idea "deletes" it from STEAMnet, all that really happens is that the user removes their name from the idea. The idea is still available to other users, it just is not associated with the user any more. As such, this request only works if the user found through your token matches the creator of the idea.

To do this, issue a DELETE request to `/ideas/id.json`. To do a delete request in Android, call `.setRequestMethod("DELETE")` on your HttpURLConnection before running it. The response of this request is the same as that of showing or creating one idea, but with no user set for its `user` attribute.

### Comments

#### Getting all Comments on a Given Jawn

To get all the comments on a given spark or idea, issue a GET request to `/sparks|ideas/id/comments.json`, where you specify if the jawn is a spark or idea, and you specify the id of the jawn in question. For instance, to get an array of all the comments on a spark with id 1, you would issue a GET request to `/sparks/1/comments.json`, to which the API would respond with something like:

	[
		{
			"id" : 1,
			"comment_text" : "Animi qui mollitia perspiciatis atque et. Inventore dolores omnis laborum. Ipsa voluptas veniam corporis omnis quisquam iste aut.",
			"created_at" : "2013-06-26T23:52:03.431Z",
			"user" : {
				"id" : 8,
				"name" : "grace",
				"email" : "tremayne@hicklehoeger.net"
			}
		},
		{
			"id" : 2,
			"comment_text" : "Voluptas neque optio facilis sint fugit. Natus sint id ipsam. Omnis veritatis ab sint vel.","created_at" : "2013-06-26T23:52:03.463Z","user" : {"id" : 6,"name" : "dan","email" : "ines@stoltenberghowell.net"}
		},
		{
			"id" : 3,
			"comment_text" : "Omnis voluptas omnis et cum non veniam eaque. Ab vero atque commodi culpa magni. Est et dignissimos non.",
			"created_at" : "2013-06-26T23:52:03.479Z",
			"user" : {
				"id" : 6,
				"name" : "dan",
				"email" : "ines@stoltenberghowell.net"
			}
		}
	]

Please note that the comments are ordered from oldest first, and that data about the spark being commented upon is omitted from the response, because the API assumes that if you know enough about the spark or idea to form the url needed for this request in the first place, then you either already know enough about the spark or idea, or you definitely know enough to issue another request to get the rest of the data that you need.

#### Posting a Comment

To post a comment on a certain spark or idea, issue a POST request to `/sparks|ideas/id/comments.json`. The only attribute you need to supply for this, other than the token of course, is `comment[comment_text]`. Although a bit redundant, this is the vital piece of information needed to form the comment. The API figures our the jawn you want to comment upon based on the url, and the user making the comment based on the token provided.

For instance, sending a POST request to `/sparks/1/comments.json?comment[comment_text]=Hello%20World` would create a comment on the spark with an id of 1, and return the following json:

	{
		"id" : 1,
		"comment_text" : "Hello world.",
		"created_at" : "2013-06-26T23:52:03.431Z",
		"user" : {
			"id" : 8,
			"name" : "grace",
			"email" : "tremayne@hicklehoeger.net"
		}
	}

#### Deleting a Comment

To delete a comment, issue a DELETE request, just as previously described, to `/sparks|ideas/id/comments/comment_id.json`, where you form the url just as in the previously described requests, with the added detail of the id of the comment you wish to delete.

Logically, this request will only work if the token provided corresponds to the user who authored this comment. Obviously we do not want users to be able to delete other users' comments. It's probably safe to assume that this kind of request will never happen, since the UI of the app should not allow this behavior, but just in case, the API verifies that the users match before deleting the comment.

If the comment was deleted successfully, the response will have an empty body with HTTP response code 204. If the users do not match, the response will have an empty body and HTTP response code 401, for unauthorized.

### Jawns

#### Getting all Jawns

To get an array of all the jawns in the databse (including both sparks and ideas), issue a GET request to `/jawns.json`. Sparks and ideas are mixed together and sorted in order of when they were created, with newer jawns first. To only get a certain number of jawns, provide the `limit` attribute. A sample response to `/jawns.json?limit=3` would be something like this:
	
	[
		{
			"id" : 2,
			"created_at" : "2013-07-13T16:29:00.694Z",
			"description" : "Rerum adipisci vel quod et incidunt excepturi suscipit.",
			"user" : {
				"id" : 1,
				"name" : "Max",
				"email" : "bryon.ryan@fisher.com"
			},
			"sparks" : [
				{
					"id" : 3,
					"created_at" : "2013-07-13T16:29:00.212Z",
					"spark_type" : "P",
					"content_type" : "L",
					"content" : "http://mraz.biz/antonetta.heel",
					"content_hash" : "6808bcc4776b5a39dbdc95d6ba34771d4d7e0b3a"
				},
				{
					"id" : 7,
					"created_at" : "2013-07-13T16:29:00.299Z",
					"spark_type" : "W",
					"content_type" : "T",
					"content" : "Molestiae amet vel velit nihil aut. Esse assumenda et praesentium nostrum. Provident odio voluptas magnam ut repellat.",
					"content_hash" : "a18ba534b698dd9ae2b3e18da3b16fb34d2c5edb"
				},
				{
					"id" : 8,
					"created_at" : "2013-07-13T16:29:00.320Z",
					"spark_type" : "I",
					"content_type" : "T",
					"content" : "Quisquam et sit. Fuga in voluptate est consequatur architecto accusamus. Blanditiis quisquam sit quam.",
					"content_hash" : "90306414b901498ad5dda28cc0c8c4f3ec2fe32c"
				},
				{
					"id" : 18,
					"created_at" : "2013-07-13T16:29:00.615Z",
					"spark_type" : "P",
					"content_type" : "V",
					"content" : "http://www.youtube.com/watch?v=j5C6X9vOEkU",
					"content_hash" : "a83cf9063c28da791497a752a2657f5881d30b3a"
				}
			],
			"comments" : [],
			"tags" : [
				"yellow",
				"legit"],
			"jawn_type" : "idea"
		},
		{
			"id" : 1,"created_at" : "2013-07-13T16:29:00.662Z","description" : "Suscipit quis est dignissimos quis cumque temporibus.",
			"user" : {
				"id" : 5,
				"name" : "Drew Leventhal",
				"email" : "grady.okon@ruel.org"
			},
			"sparks" : [
				{
					"id" : 17,
					"created_at" : "2013-07-13T16:29:00.592Z",
					"spark_type" : "I",
					"content_type" : "V",
					"content" : "http://www.youtube.com/watch?v=1VuMdLm0ccU",
					"content_hash" : "8172e08070d9e03cbf3d51c3dcb3dc310e7f0f87"
				},
				{
					"id" : 14,
					"created_at" : "2013-07-13T16:29:00.518Z",
					"spark_type" : "I",
					"content_type" : "A",
					"content" : "http://soundcloud.com/martin_lind/texture-vi",
					"content_hash" : "775acb2d18b2a529767e10b9e18ac4d0579a6506"
				},
				{
					"id" : 2,
					"created_at" : "2013-07-13T16:29:00.198Z",
					"spark_type" : "I",
					"content_type" : "L",
					"content" : "http://langoshhahn.biz/teresa.senger",
					"content_hash" : "ceb35dffb5a951fdf448d07de9e4d7bcc4a8d229"
				},
				{
					"id" : 15,
					"created_at" : "2013-07-13T16:29:00.548Z",
					"spark_type" : "P",
					"content_type" : "A",
					"content" : "http://soundcloud.com/se-beat/se-beat-straight-to-straight",
					"content_hash" : "4970380a916ccc26fb3a1c0115410646de4676b1"
				}
			],
			"comments" : [
				{
					"id" : 29,
					"comment_text" : "Quisquam ullam molestias similique error impedit. Culpa libero accusantium. Asperiores omnis eius totam illum enim ut unde.",
					"created_at" : "2013-07-13T16:29:00.691Z",
					"user" : {
						"id" : 7,
						"name" : "Heather Witzel-Lakin",
						"email" : "josue.boyle@nikolaus.net"
					}
				}
			],
			"tags" : [
				"tablets",
				"purple"
			],
			"jawn_type" : "idea"
		},
		{
			"id" : 18,
			"created_at" : "2013-07-13T16:29:00.615Z",
			"spark_type" : "P",
			"content_type" : "V",
			"content" : "http://www.youtube.com/watch?v=j5C6X9vOEkU",
			"content_hash" : "a83cf9063c28da791497a752a2657f5881d30b3a",
			"users" : [
				{
					"id" : 6,
					"name" : "Dan Stadtmauer",
					"email" : "rocio@feeneyconsidine.biz"
				},
				{
					"id" : 8,
					"name" : "Grace Heard",
					"email" : "pauline@morar.net"
				}
			],
			"ideas" : [
				{
					"id" : 2,
					"created_at" : "2013-07-13T16:29:00.694Z",
					"description" : "Rerum adipisci vel quod et incidunt excepturi suscipit."
				},
				{
					"id" : 10,
					"created_at" : "2013-07-13T16:29:00.868Z",
					"description" : "Et quo rerum laboriosam qui saepe fugit minus quasi."
				},
				{
					"id" : 14,
					"created_at" : "2013-07-13T16:29:01.011Z",
					"description" : "Dolores nostrum est dolorum exercitationem."
				}
			],
			"comments" : [
				{
					"id" : 27,
					"comment_text" : "Quaerat facere enim error architecto. Id eligendi est praesentium qui labore ipsam nisi. Aliquam praesentium perspiciatis quia velit veniam.",
					"created_at" : "2013-07-13T16:29:00.634Z",
					"user" : {
						"id" : 1,
						"name" : "Max",
						"email" : "bryon.ryan@fisher.com"
					}
				},
				{
					"id" : 28,
					"comment_text" : "Et culpa officia. Quam voluptatem aut iste. Eum et sunt odio.",
					"created_at" : "2013-07-13T16:29:00.637Z",
					"user" : {
						"id" : 1,
						"name" : "Max",
						"email" : "bryon.ryan@fisher.com"
					}
				}
			],
			"tags" : [
				"yellow",
				"rails",
				"purple"
			],
			"jawn_type" : "spark"
		}
	]

Another parameter to supply to the jawns view is `lite`, which, when set to true, results in an ultra light response, which requires exponentially fewer database calls, and results in requests that are 20x faster (in my entirely unscientific tests). It does this by only using the data immediately available for each spark and idea, without making further requests to find associated users, tags, etc. A response for a request to `/jawns.json?limit=3&lite=true` would look something like this:

	[
		{
			"id":2,
			"description":"Rerum adipisci vel quod et incidunt excepturi suscipit.",
			"created_at":"2013-07-13T16:29:00.694Z",
			"jawn_type":"idea"
		},
		{
			"id":1,
			"description":"Suscipit quis est dignissimos quis cumque temporibus.",
			"created_at":"2013-07-13T16:29:00.662Z",
			"jawn_type":"idea"
		},
		{
			"id":18,
			"spark_type":"P",
			"content_type":"V",
			"content":"http://www.youtube.com/watch?v=j5C6X9vOEkU",
			"content_hash":"a83cf9063c28da791497a752a2657f5881d30b3a",
			"created_at":"2013-07-13T16:29:00.615Z",
			"jawn_type":"spark"
		}
	]