package smallcampus.QuickPointer.bluetooth;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.*;

public class ServiceDiscoveryApp {
    public static final Vector<RemoteDevice> devicesDiscovered = new Vector<RemoteDevice>();
    
    static final UUID OBEX_OBJECT_PUSH = new UUID(0x1105);
    
    static final UUID OBEX_FILE_TRANSFER = new UUID(0x1106);

    public static final Vector<String> serviceFound = new Vector<String>();

    public static void main(String[] args) throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();

        devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                devicesDiscovered.addElement(btDevice);
                try {
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
            }

            public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };

        synchronized(inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() +  " device(s) found");
            }
        }
        
        //---service search-----------
        serviceFound.clear();

        UUID serviceUUID = OBEX_OBJECT_PUSH;
        if ((args != null) && (args.length > 0)) {
            serviceUUID = new UUID(args[0], false);
        }

        final Object serviceSearchCompletedEvent = new Object();

        DiscoveryListener dlistener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            }

            public void inquiryCompleted(int discType) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                for (int i = 0; i < servRecord.length; i++) {
                    String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (url == null) {
                        continue;
                    }
                    serviceFound.add(url);
                    DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                    if (serviceName != null) {
                        System.out.println("service " + serviceName.getValue() + " found " + url);
                    } else {
                        System.out.println("service found " + url);
                    }
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("service search completed!");
                synchronized(serviceSearchCompletedEvent){
                    serviceSearchCompletedEvent.notifyAll();
                }
            }

        };

        UUID[] searchUuidSet = new UUID[] { serviceUUID };
        int[] attrIDs =  new int[] {
                0x0100 // Service name
        };

        for(Enumeration<RemoteDevice> en = devicesDiscovered.elements(); en.hasMoreElements(); ) {
            RemoteDevice btDevice = en.nextElement();

            synchronized(serviceSearchCompletedEvent) {
                System.out.println("search services on " + btDevice.getBluetoothAddress() + " " + btDevice.getFriendlyName(false));
                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, btDevice, dlistener);
                serviceSearchCompletedEvent.wait();
            }
        }
    }
 }
