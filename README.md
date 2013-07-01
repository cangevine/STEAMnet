# STEAMnet

The API can be accessed at /api/v1/

## Creating objects

To create an object, send a push request to:

    /api/v1/class.json

where `class` is the plural of the class you are trying to create (sparks, ideas, users, comments).

with attributes in the form:

    ?class[attr1]=value1&class[attr2]=value2...?username=myusername

where `class` is the name of the class of which you are making an instance (spark, idea, user, comment), `attr` is the name of the attribute in the Rails model (content, content_type, spark_type, etc.), and `value` is the value of the attribute.

Be sure to include the `username` attribute with the currently logged in user in order to associate created comments, sparks, ideas, etc. with a certain user.

For instance, a PUSH request to create a new spark would look like this:

    /api/v1/sparks.json
        ?spark[spark_type]=W
        &spark[content_type]=T
        &spark[content]=What if kittens
        &username=max

Such a request would then return the json of the object you have just created, including its `id` and `created_at` attributes at the time at which it was saved into the databse. A more detailed view of this return can be found in the next section.

## Accessing Objects

To access all the properties of an object, send a GET request to:

    /api/v1/class/id.json

where `class` is again the plural of the class of the object you are trying to get (sparks, ideas, users, comments, tags).

For instance, to access the spark we created in the previous example, use a GET request to:

    /api/v1/sparks/1.json
    
This would then return the following json

    {
        id: 1,
        created_at: sometime,
        updated_at: sometime,
        spark_type: W,
        content_type: T,
        content: What if kittens,
        content_has: 458JMCW40Z59KJH3WP4I,
        users: {
            // a list of users who have sparked this same idea.
        }
    }

## Accessing lists of objects

To access the list of all objects of a certain class, send a GET request to:

    /api/v1/class.json

Where `class`, again, is the plural of the class you are trying to access (users, tags, sparks, ideas, comments).

## Class Descriptions

Each class in the database has properties which are settable during the creation of an object of this class, and which are returned during the GET request for this object. These properties are described here.

"*" indicates an attribute that cannot be set during creation, but is still returned while accessing the object.

### User

* email
* name

### Spark

* spark_type
* content
* content_hash
* content_type
* tags
    * passed in as a comma separated list like `tags=yellow,purple,cats`
    * returned as a list of sparks with all their attributes
* ideas*
* users*
* comments*

### Idea

* description
* tags
    * passed in as a comma separated list like `tags=yellow,purple,cats`
    * returned as a list of sparks with all their attributes
* sparks*
* users*
* comments*

### Comment

* comment_text
* user*
* commentable_object
    * passed in as `commentable_id`
    * returned as the object with all its attributes

### Tag

(note: tags are created automatically while creating sparks and ideas, they are not created using PUSH requests.

* tag_name
* sparks*
* ideas*
