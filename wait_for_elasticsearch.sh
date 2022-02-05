#!/bin/sh
  
until wget --user=$ES_USERNAME --password=$ES_PASSWORD "http://$ES_HOST:9200/_cat/health" -O '/dev/null'; do
  >&2 echo "Elasticsearch is unavailable - sleeping"
  sleep 1
done
  
>&2 echo "Elasticsearch is available - continue"
exec "$@"