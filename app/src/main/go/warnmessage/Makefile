
default: main

pb/definitions.pb.go: ./../../proto/definitions.proto
	PATH="/home/mru/go/bin:${PATH}" protoc --proto_path=./../../proto/ --go_out=./pb --go_opt=paths=source_relative definitions.proto

main: pb/definitions.pb.go main.go
	go build -o main main.go
