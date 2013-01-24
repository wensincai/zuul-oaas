package cz.cvut.authserver.oauth2.services;

import cz.cvut.authserver.oauth2.api.resources.exceptions.NoSuchResourceException;
import cz.cvut.authserver.oauth2.models.resource.Resource;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Tomas Mano <tomasmano@gmail.com>
 */
public interface ResourceService {
    
    /**
     * Checks if the given resource is registered resource.
     * 
     * @param id resource's id to be checked 
     * @return if the given resource is registered resource
     */
    public boolean isRegisteredResource(Serializable id);
    
    /**
     * Finds resource with the given id.
     * 
     * @param id resource's id
     * @return resource if exists
     * @throws NoSuchResourceException when no matching resource was found
     */
    public Resource findResourceById(String id) throws NoSuchResourceException;
    
    /**
     * Creates new resource.
     * 
     * @param resource resource to be created
     * @return created resource
     */
    public Resource createResource(Resource resource);
    
    /**
     * Update resource with the given id.
     * 
     * @param id resource's id
     * @param resource resource's content to be updated with
     * @throws NoSuchResourceException when no matching resource was found
     */
    public void updateResource(String id, Resource resource) throws NoSuchResourceException;

    /**
     * Delete resource with the given id.
     * 
     * @param id resource§s id
     * @throws NoSuchResourceException when no matching resource was found
     */
    public void deleteResourceById(String id) throws NoSuchResourceException;
    
    /**
     * Returns all resources.
     * 
     * @return all resources
     */
    public List<Resource> getAllResources();

    /**
     * Returns all public resources.
     *
     * @return all resources
     */
    public List<Resource> getAllPublicResources();
}