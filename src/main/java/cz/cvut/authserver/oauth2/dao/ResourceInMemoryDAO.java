package cz.cvut.authserver.oauth2.dao;

import cz.cvut.authserver.oauth2.api.resources.exceptions.NoSuchResourceException;
import cz.cvut.authserver.oauth2.generators.IdentificatorGenerator;
import cz.cvut.authserver.oauth2.models.resource.Auth;
import cz.cvut.authserver.oauth2.models.resource.Resource;
import cz.cvut.authserver.oauth2.models.resource.Scope;
import cz.cvut.authserver.oauth2.models.resource.enums.ResourceVisibility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ResourceInMemoryDAO implements ResourceDAO {

    private static List<Resource> resources;
    
    @Autowired
    IdentificatorGenerator identificatorGenerator;

    static {
        resources = new ArrayList<Resource>();
        populateResources();
    }

    @Override
    public boolean isRegisteredResource(Serializable id) {
        for (Resource resource : resources) {
            if (resource.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Resource createResource(Resource resource) {
        resource.setId(identificatorGenerator.generateArgBasedIdentificator(resource.getTitle()));
        resources.add(resource);
        return resource;
    }

    @Override
    public void updateResource(String id, Resource resource) throws NoSuchResourceException{
        Resource update = findResourceById(id);
        resources.remove(update);
        resources.add(resource);
    }

    @Override
    public List<Resource> getAllPublicResources() {
        List<Resource> publicResources = new ArrayList<Resource>();
        for (Resource resource : resources) {
            if (resource.getVisibility().equals(ResourceVisibility.PUBLIC.get())) {
                publicResources.add(resource);
            }
        }
        return publicResources;
    }
    
    @Override
    public List<Resource> getAllResources() {
        return resources;
    }

    @Override
    public Resource findResourceById(String id) throws NoSuchResourceException{
        for (Resource resource : resources) {
            if (resource.getId().equals(id)) {
                return resource;
            }
        }
        throw new NoSuchResourceException(String.format("No resource exists with given id [%s]", id));
    }
    
    @Override
    public boolean deleteResourceById(String id) throws NoSuchResourceException{
        Resource deleted = findResourceById(id);
        return resources.remove(deleted);
    }

    private static void populateResources() {
        Auth auth1 = createAuth(createScope("https://www.cvutapis.cz/auth/kosapi.readonly", "Read only scope", false));
        resources.add(createResource(auth1, "kos-api-basic-880693", "https://www.cvutapis.cz/kosapi/v3", "API for access to the data within KOS db.", "kosapi", "v3", "KOS API Basic"));
        Auth auth2 = createAuth(createScope("https://www.cvutapis.cz/auth/kosapi.write", "Write scope", true));
        resources.add(createResource(auth2, "kos-api-master-646521", "https://www.cvutapis.cz/kosapi/v3", "API for access to the data within KOS db.", "kosapi", "v3", "KOS API Master"));
        Auth auth3 = createAuth(createScope("https://www.cvutapis.cz/auth/kosapi.teacher", "Teacher scope", true));
        resources.add(createResource(auth3, "kos-api-teacher-296880", "https://www.cvutapis.cz/kosapi/v3", "API for access to the data within KOS db.", "kosapi", "v3", "KOS API Teacher"));
        Auth auth4 = createAuth(createScope("https://www.cvutapis.cz/auth/erasmus.readonly", "Read only scope", false));
        resources.add(createResource(auth4, "erasmus-api-429817", "https://www.cvutapis.cz/erasmusapi/v1", "API for access to the data within Erasmus db.", "erasmusapi", "v1", "Erasmus API"));
        Auth auth5 = createAuth(createScope("https://www.cvutapis.cz/auth/edux.student", "Student scope", true));
        resources.add(createResource(auth5, "edux-api-student-716690", "https://www.cvutapis.cz/eduxapi/v2", "API for access to the data within Edux db.", "eduxapi", "v2", "Edux API Student"));
        Auth auth6 = createAuth(createScope("https://www.cvutapis.cz/auth/edux.write", "Write scope", true));
        resources.add(createResource(auth6, "edux-api-pro-927661", "https://www.cvutapis.cz/eduxapi/v2", "API for access to the data within Edux db.", "eduxapi", "v2", "Edux API Pro"));
        Auth auth7 = createAuth(createScope("https://www.cvutapis.cz/auth/edux.readonly", "Read only scope", false));
        resources.add(createResource(auth7, "edux-api-simple-8876512", "https://www.cvutapis.cz/eduxapi/v2", "API for access to the data within Edux db.", "eduxapi", "v2", "Edux API Simple"));
    }

    private static Resource createResource(Auth auth, String code, String url, String desc, String name, String version, String title) {
        Resource resource = new Resource(auth, code, url, desc, name, version, title, ResourceVisibility.PUBLIC.get());
        return resource;
    }

    private static Auth createAuth(Scope... scopes) {
        Auth auth = new Auth();
        auth.setScope(Arrays.asList(scopes));
        return auth;
    }

    private static Scope createScope(String name, String description, boolean secured) {
        return new Scope(name, description, secured);
    }
    
    //////////  Getters / Setters  //////////

    public IdentificatorGenerator getIdentificatorGenerator() {
        return identificatorGenerator;
    }

    public void setIdentificatorGenerator(IdentificatorGenerator identificatorGenerator) {
        this.identificatorGenerator = identificatorGenerator;
    }
    
    
}