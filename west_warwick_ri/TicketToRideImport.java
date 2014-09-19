package org.codeforamerica.tickettoride;

// Wild card imports (lifted from example import --- TODO this is a bad coding practice, if we can narrow the classes, let's do it) 
import java.util.*;
import org.apache.ojb.broker.query.*;
import com.x2dev.sis.model.beans.*;
import com.follett.fsc.core.k12.beans.*;
import com.x2dev.sis.model.business.dictionary.*;
import com.follett.fsc.core.k12.business.dictionary.*;

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
 * <p>The import job is based on the Concord-Carlise 12 Data import job, created by X2,
 * but has been heavily modified.</p>
 *
 * <p>The import script takes the following arguments:</p>
 * <ul>
 *  <li>school_id: The ID of the school students should be assigned to</li> 
 * </ul>
 *
 * @author Code for America
 */
public class TicketToRideImport extends TextImportJavaSource
{        
    /*    
     * CSV fields, along with the destination Aspen table and field name
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
     * Objects used across methods
     */
    ModelBroker modelBroker = null; // populated in the `importData` method 
    String schoolId = null; // populated in the `importData` method
    
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

        // Get configuration parameters
        schoolId = (String) getParameter("school_id");
    
        // Do the import!
        super.importData(sourceFile);
    }

    /**
     * @see com.x2dev.sis.tools.imports.TextImportJavaSource#importRecord(java.util.List, int)
     */
    protected void importRecord(List record, int lineNumber) throws Exception
    {
        /*
         * Check if the record represents a new student, create beans if necessary
         */
        Criteria criteria = new Criteria();
        criteria.addEqualTo(SisStudent.COL_STATE_ID, record.get(INDEX_SASID));
        
        QueryByCriteria query = new QueryByCriteria(SisStudent.class, criteria);
        SisStudent student = (SisStudent) getBroker().getBeanByQuery(query);
        if (student == null)
        {
            SisAddress address = (SisAddress) X2BaseBean.newInstance(SisAddress.class, getBroker().getPersistenceKey());
            SisPerson person = (SisPerson) X2BaseBean.newInstance(SisPerson.class, getBroker().getPersistenceKey());
            student = (SisStudent) X2BaseBean.newInstance(SisStudent.class, getBroker().getPersistenceKey());
            
            student.setHonorRollIncludeIndicator(true);

            setAddressFields(address, record);
            
            person.setPhysicalAddressOid(address.getOid());
            setPersonFields(person, record);
            
            student.setPersonOid(person.getOid());
            setStudentFields(student, record);
            
            StudentEnrollment enrollment = (StudentEnrollment) X2BaseBean.newInstance(StudentEnrollment.class, getBroker().getPersistenceKey());
            enrollment.setStudentOid(student.getOid());
            setEnrollmentFields(enrollment);
            
            incrementInsertCount();
        }
        else
        {
            SisPerson person = (SisPerson) student.getPerson();
            SisAddress address =  (SisAddress) (person.getPhysicalAddress());
            
            setAddressFields(address, record);
            setPersonFields(person, record);
            setStudentFields(student, record);
            
            incrementMatchCount();
            incrementUpdateCount();
        }
    }

    /**
     * Returns the numeric value for the given grade level.
     * 
     * @param gradeLevel
     * 
     * @return int
     */
    private int getGradeOffset(String gradeLevel)
    {
        HashMap stateToOffsetCodes = null;
        
        DataDictionary dictionary = DataDictionary.getDistrictDictionary(getBroker().getPersistenceKey());
        DataDictionaryField field = 
            dictionary.findDataDictionaryField(SisStudent.class.getName(), SisStudent.COL_GRADE_LEVEL);
        
        if (field.hasReferenceTable())
        {
            Collection codes = field.getReferenceTable().getReferenceCodes();
            stateToOffsetCodes = new HashMap((int) (codes.size() * 1.5));
            Iterator codeIterator = codes.iterator();
            
            while (codeIterator.hasNext())
            {
                ReferenceCode code = (ReferenceCode) codeIterator.next();
                if (!StringUtils.isEmpty(code.getStateCode()))
                {
                    stateToOffsetCodes.put(code.getStateCode(), code.getFieldA005());
                }
            }
        }
        
        String yogOffset = (String) stateToOffsetCodes.get(gradeLevel);
        
        int gradeOffset = 4;
        
        if (StringUtils.isNumeric(yogOffset))
        {
            gradeOffset = Integer.parseInt(yogOffset);
        }
        
        return gradeOffset;
    }

    /**
     * Sets the address fields as given by the input.
     * 
     * @param address
     * @param record
     */
    private void setAddressFields(SisAddress address, List record)
    {
        if (address != null)
        {
//          Commented out following line because it generated an error for Carlisle import, 4/13/11
//            address.setOrganization1Oid(((SisOrganization) getOrganization()).getRootOrganzation().getOid());
            
            String streetNumber = (String) record.get(INDEX_STREET_NUMBER);
            String streetName = (String) record.get(INDEX_STREET_NAME);
            
            String line1 = streetNumber + " " + streetName;
            AddressParser.parseLine01((SisOrganization) getOrganization(), line1, address, true, getBroker());
            
            address.setAddressLine02((String) record.get(INDEX_ADDRESS_LINE2));
            
            address.setCity((String) record.get(INDEX_CITY));
            address.setState((String) record.get(INDEX_STATE));
            address.setPostalCode((String) record.get(INDEX_ZIP));

            address.setAddressLine03(AddressParser.format((SisOrganization) getOrganization(), 3, null, address, getBroker()));
            
            m_modelBroker.saveBeanForced(address);
        }
    }
    
    /**
     * Sets the enrollment fields.
     * 
     * @param enrollment
     */
    private void setEnrollmentFields(StudentEnrollment enrollment)
    {
        enrollment.setEnrollmentType("E");
        enrollment.setEnrollmentDate(m_date);
        enrollment.setSchoolOid(((SisSchool) getSchool()).getOid());
        enrollment.setStatusCode(m_statusCode);
        enrollment.setYog(enrollment.getStudent().getYog());
        
        m_modelBroker.saveBeanForced(enrollment);
    }
    
    /**
     * Sets the person fields as given by the input.
     * 
     * @param person
     * @param record
     */
    private void setPersonFields(SisPerson person, List record)
    {
        if (person != null)
        {
//          Commented out following line because it generated an error for Carlisle import, 4/13/11
//            person.setOrganization1Oid(((SisOrganization) getOrganization()).getRootOrganzation().getOid());
            person.setFirstName((String) record.get(INDEX_FIRST_NAME));
            person.setLastName((String) record.get(INDEX_LAST_NAME));
            person.setPhone01((String) record.get(INDEX_PHONE));
            
            m_modelBroker.saveBeanForced(person);
        }
    }
    
    /**
     * Sets the student fields as given by the input.
     * 
     * @param student
     * @param record
     */
    private void setStudentFields(SisStudent student, List record)
    {
        if (student != null)
        {
//          Commented out following line because it generated an error for Carlisle import, 4/13/11
//           student.setOrganization1Oid(((SisOrganization) getOrganization()).getRootOrganzation().getOid());
            student.setSchoolOid(((SisSchool) getSchool()).getOid());
            student.setNextSchoolOid(((SisSchool) getSchool()).getOid());
            student.setEnrollmentStatus(m_statusCode);
            student.setEnrollmentTypeCode("Carlisle");
            student.setStateId((String) record.get(INDEX_SASID));
            
            String gradeLevel = (String) record.get(INDEX_GRADE);
            int numericEquivalent = getGradeOffset(gradeLevel);
            student.setYog(((SisOrganization) getOrganization()).getCurrentContext().getSchoolYear() + 12 - numericEquivalent);
            student.setGradeLevel(gradeLevel);
            
            student.setFieldC001((String) record.get(INDEX_PARENTS));
            m_modelBroker.saveBeanForced(student);
        }
    }
}