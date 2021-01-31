package main

import (
	"time"
	"net"

	"github.com/golang/protobuf/proto"
	"github.com/matthinc/BachelorThesisProject/pb"
)

func main() {
	tst := time.Now().Unix()

	wrapper := &pb.LocationMessageWrapper{
		Msg: &pb.LocationMessageWrapper_Message{
			&pb.WarnMessage{
				Message: "Road closed due to accident.",
				ValidUntil: tst + 120, // Two minutes
				Latitude: 48.1658329,
				Longitude: 11.5765968,
			},
		},
	}

	bytes, _ := proto.Marshal(wrapper)

	socket, _ := net.ListenPacket("udp4", ":1510")
	defer socket.Close()
	
	ip ,_ := net.ResolveUDPAddr("udp4", "192.168.1.255:1510")

	socket.WriteTo(bytes, ip)
}
