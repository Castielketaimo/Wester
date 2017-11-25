package ca.bcit.wester.models;

import java.util.ArrayList;

/**
 * Service Model for the Service Table
 * @author Team <Void>.
 */

public class Service
{
    /** ID of the service */
    private int _serviceID;
    /** Longitude of the service. */
    private double _Longitude;
    /** Latitude of the service . */
    private double _Latitude;
    /** Name of the service. */
    private String _Name;
    /** Category of the service. */
    private String _Category;
    /** Tags of the service. */
    private ArrayList<String> _Tags;
    /** Description of the service. */
    private String _Description;
    /** Service hours of the service. */
    private String _Hours;
    /** Location address of the service. */
    private String _Address;
    /** Postal code of the service. */
    private String _PostalCode;
    /** Phone of the service. */
    private String _Phone;
    /** Email of the service. */
    private String _Email;
    /** Website of the service. */
    private String _Website;

    /**
     * Constructor for Service instance
     * @param ServiceID - id of the service
     * @param Longitude - longitude of the _Location for the service
     * @param Latitude - latitude of the _Location for the service
     * @param Name - service's Name
     * @param Tags - tags of the services
     * @param Category - service's category
     * @param Description - service's description
     * @param Hours - hours of services
     * @param Address - service's address
     * @param PostalCode - service's postal code
     * @param Phone - service's phone
     * @param Email - service's email
     * @param Website - service's website
     */
    public Service(int ServiceID, String Name, double Longitude, double Latitude, ArrayList<String> Tags, String Category, String Description, String Hours, String Address, String PostalCode, String Phone, String Email, String Website)
    {
        this._serviceID = ServiceID;
        this._Longitude = Longitude;
        this._Latitude = Latitude;
        this._Name = Name;
        this._Category = Category;
        this._Description = Description;
        this._Hours = Hours;
        this._Address = Address;
        this._PostalCode = PostalCode;
        this._Phone = Phone;
        this._Email = Email;
        this._Website = Website;
        this._Tags = Tags;
    }

    /**
     * Getter for the service id
     * @return - the id of the service
     */
    public int getID() {
        return _serviceID;
    }

    /**
     * Getter for the longitude of the service
     * @return - the longitude as a double
     */
    public double getLongitude()
    {
        return _Longitude;
    }

    /**
     * Setter for the longitude
     * @param Longitude - Longitude to be set
     */
    public void setLongitude(double Longitude)
    {
        this._Longitude = Longitude;
    }

    /**
     * Getter for the latitude of the service
     * @return - the latitude as double
     */
    public double getLatitude()
    {
        return _Latitude;
    }

    /**
     * Setter for the latitude of the service
     * @param Latitude - Latitude to be set
     */
    public void setLatitude(double Latitude)
    {
        this._Latitude = Latitude;
    }

    /**
     * Getter for the _Name of the service.
     * @return - the Name of service as a string.
     */
    public String getName()
    {
        return _Name;
    }

    /**
     * Setter for the Name of the service.
     * @param Name - New name for the service.
     */
    public void setName(String Name)
    {
        this._Name = Name;
    }

    /**
     * Getter for the category of the service.
     * @return - Category of the service
     */
    public String getCategory()
    {
        return _Category;
    }

    /**
     * Setter for the category of the service
     * @param Category - Category to be set
     */
    public void setCategory(String Category)
    {
        this._Category = Category;
    }

    /**
     * Getter for the tags
     * @return - ArrayList of tags
     */
    public ArrayList<String> getTags() {
        return _Tags;
    }

    /**
     * Adding new tag for the service
     * @param Tag - New tag to be added
     */
    public void addTag(String Tag)
    {
        _Tags.add(Tag);
    }

    /**
     * Returns if the service contains the passed tag
     * @param Tag - The tag being checked against
     * @return - boolean seeing if contains the passed tag
     */
    public boolean hasTag(String Tag)
    {
        return _Tags.contains(Tag);
    }

    /**
     * Delete the passed tag
     * @param Tag - The tag being deleted
     * @return - true if succeeded else false
     */
    public boolean deleteTag(String Tag)
    {
        return _Tags.remove(Tag);
    }
    /**
     * Getter for the description of the service
     * @return - the description of the service as string
     */
    public String getDescription()
    {
        return _Description;
    }

    /**
     * Setter for the description of the service
     * @param Description - New description for the service
     */
    public void setDescription(String Description)
    {
        this._Description = Description;
    }

    /**
     * Getter for the hours of the service
     * @return - the hours as a string
     */
    public String getHours()
    {
        return _Hours;
    }

    /**
     * Setter for the hours of service
     * @param Hours - The new hours for the service
     */
    public void setHours(String Hours)
    {
        this._Hours = Hours;
    }

    /**
     * Getter for the Address of the service
     * @return - the address as a string
     */
    public String getAddress()
    {
        return _Address;
    }

    /**
     * Setter for the Address of service
     * @param Address - The address to be set
     */
    public void setAddress(String Address)
    {
        this._Address = Address;
    }

    /**
     * Getter for the postal code of the service
     * @return - the postal code as a string
     */
    public String getPostalCode()
    {
        return _PostalCode;
    }

    /**
     * Setter for the Postal Code of service
     * @param PostalCode - The postal code to be set
     */
    public void setPostalCode(String PostalCode)
    {
        this._PostalCode = PostalCode;
    }

    /**
     * Getter for the phone of the service
     * @return - the phone as a string
     */
    public String getPhone()
    {
        return _Phone;
    }

    /**
     * Setter for the phone of service
     * @param Phone - The phone to be set
     */
    public void setPhone(String Phone)
    {
        this._Phone = Phone;
    }

    /**
     * Getter for the email of the service
     * @return - the email as a string
     */
    public String getEmail()
    {
        return _Email;
    }

    /**
     * Setter for the email of service
     * @param Email - The email to be set
     */
    public void setEmail(String Email)
    {
        this._Email = Email;
    }

    /**
     * Getter for the Website of the service
     * @return - the website as a string
     */
    public String getWebsite()
    {
        return _Website;
    }

    /**
     * Setter for the website of service
     * @param Website - The website to be set
     */
    public void setWebsite(String Website)
    {
        this._Website = Website;
    }
}
