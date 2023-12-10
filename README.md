# Quote Kameleoon trial task

## Run application
Run this command: 'docker-compose up' in command line

## API

### Create user
POST request '/api/user' required JSON:
```JSON
{
  "name":
  "email":
  "password":
}
```

### Create quote
POST request '/api/quote' required JSON:
```JSON
{
  "content":
  "userId":
}
```

### Get random quote
GET request '/api/quote/random'

### Get quote by id
GET request '/api/quote/{id}'

### Update quote
PUT request '/api/quote/{id}' required JSON:
```JSON
{
  "content":
  "userId":
}
```

### Delete quote
DELETE request '/api/quote/{id}'


### Vote for quote
POST request '/api/quote/{id}/vote?userId={userId}&isUpvote={true/false}' 
#### Request param
- userId - user id
- isUpvote - true upvote false downvote

### Get top 10 quotes
GET request '/api/quote/top10'

### Get worst 10 quotes
GET request '/api/quote/worst10'
