//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see http://www.gnu.org/licenses/.
// 

//
// TODO auto-generated module
//
package crownet.emulation;

import inet.applications.contract.IApp;

simple DensityMessageHandler like IApp
{
    parameters:
        @display("i=block/sink");
        @class(crownet::DensityMessageHandler);
        string interfaceTableModule;
        double startTime @unit(s) = default(0s); // application start time
        double stopTime @unit(s) = default(-1s);  // time of finishing receiving
        int localPort;
        int externalPort;
        string localAddress;
        string externalAddress;
        int offsetNorthing;
        int offsetEasting;
        
    gates:
        input socketIn @labels(UdpControlInfo/up);
        output socketOut @labels(UdpControlInfo/down);
        input socketInExternal @labels(UdpControlInfo/up);
        output socketOutExternal @labels(UdpControlInfo/down);
}
