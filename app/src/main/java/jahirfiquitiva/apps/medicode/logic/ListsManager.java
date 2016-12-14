/*
 * Copyright (c) 2016. Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jahirfiquitiva.apps.medicode.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import jahirfiquitiva.apps.medicode.logic.objects.Appntmnt;
import jahirfiquitiva.apps.medicode.logic.objects.Doctor;
import jahirfiquitiva.apps.medicode.logic.objects.Patient;
import jahirfiquitiva.apps.medicode.logic.objects.Person;

/**
 * @author Jahir Fiquitiva
 */
@SuppressWarnings("unchecked")
public class ListsManager implements Serializable {

    private static final long serialVersionUID = 123L;

    private ArrayList<Doctor> doctors;
    private ArrayList<Patient> patients;
    private ArrayList<Appntmnt> appntmnts;

    private boolean modified = false;

    public ListsManager() {
        this.doctors = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.appntmnts = new ArrayList<>();
    }

    public boolean addDoctor(Doctor doctor) {
        if (findDoctorIndex(doctor) >= 0) return false;
        doctors.add(doctor);
        modified = true;
        return true;
    }

    public boolean addPatient(Patient patient) {
        if (findPatientIndex(patient) >= 0) return false;
        patients.add(patient);
        modified = true;
        return true;
    }

    public boolean addAppntmnt(Appntmnt appointment, boolean checkDoctor) {
        if (appntmntsContainsAppntmnt(appointment, checkDoctor)) return false;
        appntmnts.add(appointment);
        modified = true;
        return true;
    }

    public ArrayList<Doctor> getDoctors() {
        return (ArrayList<Doctor>) doctors.clone();
    }

    public ArrayList<Patient> getPatients() {
        return (ArrayList<Patient>) patients.clone();
    }

    public ArrayList<Appntmnt> getAppntmnts() {
        return (ArrayList<Appntmnt>) appntmnts.clone();
    }

    public ArrayList<Appntmnt> getDoctorAppntmnts(Doctor doctor) {
        ArrayList<Appntmnt> list = new ArrayList<>();
        for (Appntmnt appntmnt : getAppntmnts()) {
            if (appntmnt.getDoctor().equals(doctor)) {
                list.add(appntmnt);
            }
        }
        return list;
    }

    public ArrayList<Appntmnt> getPatientAppntmnts(Patient patient) {
        ArrayList<Appntmnt> list = new ArrayList<>();
        for (Appntmnt appntmnt : getAppntmnts()) {
            if (appntmnt.getPatient().equals(patient)) {
                list.add(appntmnt);
            }
        }
        return list;
    }

    public int findDoctorIndex(Doctor doctor) {
        return Collections.binarySearch(sortDoctors(), doctor, Person.personComparator);
    }

    private ArrayList<Doctor> sortDoctors() {
        ArrayList<Doctor> docs = getDoctors();
        Collections.sort(docs, Person.personComparator);
        return docs;
    }

    public int findPatientIndex(Patient patient) {
        return Collections.binarySearch(sortPatients(), patient, Person.personComparator);
    }

    private ArrayList<Patient> sortPatients() {
        ArrayList<Patient> pats = getPatients();
        Collections.sort(pats, Person.personComparator);
        return pats;
    }

    public int findAppntmntIndex(Appntmnt appntmnt) {
        return Collections.binarySearch(sortAppntmnts(), appntmnt, Appntmnt.appntmntComparator);
    }

    private ArrayList<Appntmnt> sortAppntmnts() {
        ArrayList<Appntmnt> appnts = getAppntmnts();
        Collections.sort(appnts, Appntmnt.appntmntComparator);
        return appnts;
    }

    public static String makeNameCamelCase(String name) {
        String[] text = name.split("\\s+");
        StringBuilder sb = new StringBuilder();
        if (text[0].length() > 0) {
            sb.append(Character.toUpperCase(text[0].charAt(0))).append(text[0].subSequence(1,
                    text[0].length()).toString().toLowerCase());
            for (int i = 1; i < text.length; i++) {
                sb.append(" ");
                sb.append(text[i].substring(0, 1).toUpperCase());
                sb.append(text[i].substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    public boolean hasBeenModified() {
        return modified;
    }

    private boolean appntmntsContainsAppntmnt(Appntmnt nAppntmnt, boolean checkDoctor) {
        boolean contains = false;
        for (Appntmnt appntmnt : getAppntmnts()) {
            if (appntmnt.equals(nAppntmnt)) {
                if (checkDoctor) {
                    contains = (appntmnt.getDoctor().equals(nAppntmnt.getDoctor()));
                } else {
                    contains = (appntmnt.getPatient().equals(nAppntmnt.getPatient()));
                }
                if (contains) break;
            }
        }
        return contains;
    }

}