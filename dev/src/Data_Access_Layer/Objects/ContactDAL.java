package Data_Access_Layer.Objects;

import DTO.ContactDTO;
import enums.ContactMethod;

import java.util.Map;

public class ContactDAL {
    private final String name;
    private final Map<ContactMethod, String> contactMethods;

    public ContactDAL(String name, Map<ContactMethod, String> contactMethods) {
        this.name = name;
        this.contactMethods = contactMethods;
    }

    public ContactDAL(ContactDTO dto) {
        this.name = dto.getName();
        this.contactMethods = dto.getContactMethods();
    }

    public ContactDTO DTO(){
        return new ContactDTO(name, contactMethods);
    }

    public void updateMethod(ContactMethod method, String data){
        if(contactMethods.containsKey(method)){
            contactMethods.put(method,data);
        }
    }

    public void addMethod(ContactMethod method, String data) {
        if(!contactMethods.containsKey(method)){
            contactMethods.putIfAbsent(method,data);
        }
    }

    public void removeMethod(ContactMethod method) {
        if(contactMethods.containsKey(method)){
            contactMethods.remove(method);
        }
    }
}
