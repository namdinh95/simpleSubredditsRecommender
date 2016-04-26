/* 
 * Load JSON and output a table which has the number of comments
 * each user has on each subreddit.
 */

reddit = load 'reddit.json' using JsonLoader('archived:boolean, author:chararray, author_flair_css_class:chararray, author_flair_text:chararray, body:chararray, controversiality:int, created_utc:bytearray, distinguished:chararray, downs:int, edited:bytearray, gilded:int, id:chararray, link_id:chararray, name:chararray, parent_id:chararray, retrieved_on:chararray, score:int, score_hidden:boolean, subreddit:chararray, subreddit_id:chararray, ups:int');

necessary = FOREACH reddit GENERATE author, body, subreddit;

grouped = GROUP necessary BY (author, subreddit);

counted = FOREACH grouped GENERATE group, COUNT(necessary);

final = FOREACH counted GENERATE group.author, group.subreddit, $1;
