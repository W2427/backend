## Test

> $ curl --header "Content-Type: application/json" --request POST --data '{"username":"super","password":"super"}' http://127.0.0.1:8810/authorizations

or

> $ curl -H "Content-Type: application/json" -d '{"username":"super","password":"super"}' http://127.0.0.1:8810/authorizations

for short (`--request POST` is default when `-d` has been used).
