package org.codeforamerica.tickettoride;

// Wild card imports (lifted from example import --- TODO this is a bad coding practice, if we can narrow the classes, let's do it) 
import java.util.*;
import org.apache.ojb.broker.query.*; //TODO Apache OJB is retired as of 2011
import com.x2dev.sis.model.beans.*;
import com.follett.fsc.core.k12.beans.*;
import com.x2dev.sis.model.business.dictionary.*;
import com.follett.fsc.core.k12.business.dictionary.*;

// Added from wild card imports
import com.x2dev.sis.model.beans.SisStudent;
import com.x2dev.sis.model.beans.SisPerson;

import com.follett.fsc.core.k12.business.ModelBroker;
import com.follett.fsc.core.k12.beans.X2BaseBean;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;


// Specific imports
import java.io.File;
import com.follett.fsc.core.k12.beans.BeanManager.PersistenceKey;
import com.follett.fsc.core.k12.business.ModelBroker;
import com.follett.fsc.core.k12.tools.imports.TextImportJavaSource;
import com.follett.fsc.core.k12.web.presentation.AddressParser;
import com.x2dev.utils.StringUtils;
import com.x2dev.utils.types.PlainDate;

/**
 * <p>This job imports a new student and 3-4 contact people.</p>
 *
 * <p>
 *   The import job is based on the Concord-Carlise 12 Data import job, created by X2,
 *   but has been modified.
 * </p>
 *
 * @author Code for America
 */
public class TicketToRideImport extends TextImportJavaSource
{        
    /*    
     * CSV fields in the Ticket to RIDE export file
     * Use *.ordinal() method to get column index number
     */
    private enum Fields
    {
        STUDENT_FIRST_NAME,
        STUDENT_MIDDLE_NAME,
        STUDENT_LAST_NAME,
        STUDENT_BIRTHDAY,
        STUDENT_PRIMARY_LANGUAGE,
        STUDENT_HOME_LANGUAGE,
        STUDENT_PRIOR_SCHOOL,
        STUDENT_HAS_IEP,
        STUDENT_HAS_504,
        STUDENT_GRADUATION_YEAR,
        STUDENT_STREET_ADDRESS_1,
        STUDENT_STREET_ADDRESS_2,
        STUDENT_CITY,
        STUDENT_STATE,
        STUDENT_ZIP_CODE,
        STUDENT_GENDER,
        STUDENT_RACES,
        STUDENT_BIRTH_PLACE,
        CONTACT_PERSON_1_FIRST_NAME,
        CONTACT_PERSON_1_LAST_NAME,
        CONTACT_PERSON_1_STREET_ADDRESS_1,
        CONTACT_PERSON_1_STREET_ADDRESS_2,
        CONTACT_PERSON_1_CITY,
        CONTACT_PERSON_1_STATE,
        CONTACT_PERSON_1_ZIP_CODE,
        CONTACT_PERSON_1_PHONE_1,
        CONTACT_PERSON_1_PHONE_1_CAN_SMS,
        CONTACT_PERSON_1_PHONE_2,
        CONTACT_PERSON_1_PHONE_2_CAN_SMS,
        CONTACT_PERSON_1_PHONE_3,
        CONTACT_PERSON_1_PHONE_3_CAN_SMS,
        CONTACT_PERSON_1_EMAIL,
        CONTACT_PERSON_1_LIVES_WITH_STUDENT,
        CONTACT_PERSON_1_CAN_RECEIVE_GRADE_NOTIFICATIONS,
        CONTACT_PERSON_1_CAN_RECEIVE_CONDUCT_NOTIFICATIONS,
        CONTACT_PERSON_1_CAN_RECEIVE_OTHER_MAILINGS,
        CONTACT_PERSON_1_CAN_PICKUP_STUDENT_FROM_SCHOOL,
        CONTACT_PERSON_1_RELATIONSHIP_TO_STUDENT,
        CONTACT_PERSON_1_MILITARY_STATUS,
        CONTACT_PERSON_1_IS_GUARDIAN,        
        CONTACT_PERSON_2_FIRST_NAME,
        CONTACT_PERSON_2_LAST_NAME,
        CONTACT_PERSON_2_STREET_ADDRESS_1,
        CONTACT_PERSON_2_STREET_ADDRESS_2,
        CONTACT_PERSON_2_CITY,
        CONTACT_PERSON_2_STATE,
        CONTACT_PERSON_2_ZIP_CODE,
        CONTACT_PERSON_2_PHONE_1,
        CONTACT_PERSON_2_PHONE_1_CAN_SMS,
        CONTACT_PERSON_2_PHONE_2,
        CONTACT_PERSON_2_PHONE_2_CAN_SMS,
        CONTACT_PERSON_2_PHONE_3,
        CONTACT_PERSON_2_PHONE_3_CAN_SMS,
        CONTACT_PERSON_2_EMAIL,
        CONTACT_PERSON_2_LIVES_WITH_STUDENT,
        CONTACT_PERSON_2_CAN_RECEIVE_GRADE_NOTIFICATIONS,
        CONTACT_PERSON_2_CAN_RECEIVE_CONDUCT_NOTIFICATIONS,
        CONTACT_PERSON_2_CAN_RECEIVE_OTHER_MAILINGS,
        CONTACT_PERSON_2_CAN_PICKUP_STUDENT_FROM_SCHOOL,
        CONTACT_PERSON_2_RELATIONSHIP_TO_STUDENT,
        CONTACT_PERSON_2_MILITARY_STATUS,
        CONTACT_PERSON_2_IS_GUARDIAN,
        CONTACT_PERSON_3_FIRST_NAME,
        CONTACT_PERSON_3_LAST_NAME,
        CONTACT_PERSON_3_STREET_ADDRESS_1,
        CONTACT_PERSON_3_STREET_ADDRESS_2,
        CONTACT_PERSON_3_CITY,
        CONTACT_PERSON_3_STATE,
        CONTACT_PERSON_3_ZIP_CODE,
        CONTACT_PERSON_3_PHONE_1,
        CONTACT_PERSON_3_PHONE_1_CAN_SMS,
        CONTACT_PERSON_3_PHONE_2,
        CONTACT_PERSON_3_PHONE_2_CAN_SMS,
        CONTACT_PERSON_3_PHONE_3,
        CONTACT_PERSON_3_PHONE_3_CAN_SMS,
        CONTACT_PERSON_3_EMAIL,
        CONTACT_PERSON_3_LIVES_WITH_STUDENT,
        CONTACT_PERSON_3_CAN_RECEIVE_GRADE_NOTIFICATIONS,
        CONTACT_PERSON_3_CAN_RECEIVE_CONDUCT_NOTIFICATIONS,
        CONTACT_PERSON_3_CAN_RECEIVE_OTHER_MAILINGS,
        CONTACT_PERSON_3_CAN_PICKUP_STUDENT_FROM_SCHOOL,
        CONTACT_PERSON_3_RELATIONSHIP_TO_STUDENT,
        CONTACT_PERSON_3_MILITARY_STATUS,
        CONTACT_PERSON_3_IS_GUARDIAN,
        CONTACT_PERSON_4_FIRST_NAME,
        CONTACT_PERSON_4_LAST_NAME,
        CONTACT_PERSON_4_STREET_ADDRESS_1,
        CONTACT_PERSON_4_STREET_ADDRESS_2,
        CONTACT_PERSON_4_CITY,
        CONTACT_PERSON_4_STATE,
        CONTACT_PERSON_4_ZIP_CODE,
        CONTACT_PERSON_4_PHONE_1,
        CONTACT_PERSON_4_PHONE_1_CAN_SMS,
        CONTACT_PERSON_4_PHONE_2,
        CONTACT_PERSON_4_PHONE_2_CAN_SMS,
        CONTACT_PERSON_4_PHONE_3,
        CONTACT_PERSON_4_PHONE_3_CAN_SMS,
        CONTACT_PERSON_4_EMAIL,
        CONTACT_PERSON_4_LIVES_WITH_STUDENT,
        CONTACT_PERSON_4_CAN_RECEIVE_GRADE_NOTIFICATIONS,
        CONTACT_PERSON_4_CAN_RECEIVE_CONDUCT_NOTIFICATIONS,
        CONTACT_PERSON_4_CAN_RECEIVE_OTHER_MAILINGS,
        CONTACT_PERSON_4_CAN_PICKUP_STUDENT_FROM_SCHOOL,
        CONTACT_PERSON_4_RELATIONSHIP_TO_STUDENT,
        CONTACT_PERSON_4_MILITARY_STATUS,
        CONTACT_PERSON_4_IS_GUARDIAN;
    }

    /*
     * Contants
     */ 
    private static final String SCHOOL_ID = "SKL000000L2Dt8";

    /*
     * Objects used across methods
     */
    ModelBroker modelBroker = null; // populated in the `importData` method 
    
    /**
     * @see com.x2dev.sis.tools.imports.TextImportJavaSource#getFieldCount()
     */
    protected int getFieldCount()
    {
        return Fields.values().length;
    }

    /**
     * @see com.x2dev.sis.tools.imports.ImportJavaSource#importData(java.io.File)
     */
    protected void importData(File sourceFile) throws Exception
    {
        // Initialize the model broker, which helps get things from the database
        modelBroker = new ModelBroker(getPrivilegeSet());

        // Do the import!
        super.importData(sourceFile);
    }

    /**
     * @see com.x2dev.sis.tools.imports.TextImportJavaSource#importRecord(java.util.List, int)
     */
    protected void importRecord(List<String> record, int lineNumber) throws Exception
    {
        // Student address fields
        SisAddress address = (SisAddress) X2BaseBean.newInstance(SisAddress.class, getBroker().getPersistenceKey());
        setStudentAddressFields(address, record);
        
        // Student name and phone fields
        SisPerson person = (SisPerson) X2BaseBean.newInstance(SisPerson.class, getBroker().getPersistenceKey());
        person.setPhysicalAddressOid(address.getOid());
        setPersonFields(person, record);
        
        // Student grade level and some enrollment statuses
        SisStudent student = (SisStudent) X2BaseBean.newInstance(SisStudent.class, getBroker().getPersistenceKey());
        student.setPersonOid(person.getOid());
        setStudentFields(student, record);
        
        StudentEnrollment enrollment = (StudentEnrollment) X2BaseBean.newInstance(StudentEnrollment.class, getBroker().getPersistenceKey());
        enrollment.setStudentOid(student.getOid());
        setEnrollmentFields(enrollment);
        
        incrementInsertCount();
    }

    /**
     * Populate the student's address fields based on the record
     * 
     * @param address A new SisAddress
     * @param record A record
     */
    private void setStudentAddressFields(SisAddress address, List<String> record)
    {

        String streetAddressLine1 = record.get( Fields.STUDENT_STREET_ADDRESS_1.ordinal() );
        String streetAddressLine2 = record.get( Fields.STUDENT_STREET_ADDRESS_2.ordinal() );
        String city = record.get( Fields.STUDENT_CITY.ordinal() );
        String state = record.get( Fields.STUDENT_CITY.ordinal() );
        String zipCode = record.get( Fields.STUDENT_ZIP_CODE.ordinal() );

        address.setAddressLine01(streetAddressLine1);
        address.setAddressLine02(streetAddressLine2);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(zipCode);

        modelBroker.saveBeanForced(address);
    }

    /**
     * Sets the student's name and phone fields
     * 
     * @param person An SisPerson object
     * @param record CSV row
     */
    private void setPersonFields(SisPerson person, List<String> record)
    {
        // Student name
        String firstName = record.get( Fields.STUDENT_FIRST_NAME.ordinal() );
        String middleName = record.get( Fields.STUDENT_MIDDLE_NAME.ordinal() );
        String lastName = record.get( Fields.STUDENT_LAST_NAME.ordinal() );

        person.setFirstName( firstName );
        person.setMiddleName( middleName );
        person.setLastName( lastName );

        // Use the phone number from the primary contact (phone1 is the only required one in Ticket to RIDE)
        String phone1 = record.get( Fields.CONTACT_PERSON_1_PHONE_1.ordinal() );
        String phone2 = record.get( Fields.CONTACT_PERSON_1_PHONE_2.ordinal() );
        String phone3 = record.get( Fields.CONTACT_PERSON_1_PHONE_3.ordinal() );

        person.setPhone01( phone1 );
        
        if( StringUtils.isEmpty(phone2) )
        {
            person.setPhone02( phone2 );
        }

        if( StringUtils.isEmpty(phone3) )
        {
            person.setPhone03( phone3 );
        }

        modelBroker.saveBeanForced(person);
    }

    /**
     * Sets the student fields as given by the input.
     * 
     * @param student
     * @param record
     */
    private void setStudentFields(SisStudent student, List<String> record)
    {
        student.setSchoolOid(SCHOOL_ID);
        student.setNextSchoolOid(SCHOOL_ID);
        student.setEnrollmentStatus("Active"); //TODO Does this need to be specified?
        student.setEnrollmentTypeCode("Enrolled"); //TODO What is this value?
        
        student.setYog(2026); //TODO filler
        student.setGradeLevel("01"); //TODO filler (but what value would kindergarten be? 0, 1, other? pre-kindergarten?)
        
        modelBroker.saveBeanForced(student);
    }
    
    /**
     * Sets the enrollment fields.
     * 
     * @param enrollment
     */
    private void setEnrollmentFields(StudentEnrollment enrollment)
    {
        enrollment.setEnrollmentType("E"); 
        enrollment.setEnrollmentDate( new PlainDate() ); //Sets to today
        enrollment.setSchoolOid(SCHOOL_ID);
        enrollment.setStatusCode("Active");
        enrollment.setYog(enrollment.getStudent().getYog());
        enrollment.setReasonCode("Enrolled");
        
        modelBroker.saveBeanForced(enrollment);
    }
}