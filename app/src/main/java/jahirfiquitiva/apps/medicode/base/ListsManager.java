/*
 * Copyright (c) 2016. Jahir Fiquitiva
 *
 * 	Licensed under the CreativeCommons Attribution-ShareAlike
 * 	4.0 International License. You may not use this file except in compliance
 * 	with the License. You may obtain a copy of the License at
 *
 * 	   http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 */

package jahirfiquitiva.apps.medicode.base;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ListsManager {

    private ArrayList<Doctor> doctors;
    private ArrayList<Patient> patients;
    private ArrayList<Appntmnt> appntmnts;

    public ListsManager() {
        this.doctors = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.appntmnts = new ArrayList<>();
    }

    public boolean addDoctor(Doctor doctor) {
        if (doctors.contains(doctor)) {
            return false;
        } else {
            doctors.add(doctor);
            return true;
        }
    }

    public boolean addPatient(Patient patient) {
        if (patients.contains(patient)) {
            return false;
        } else {
            patients.add(patient);
            return true;
        }
    }

    public boolean addAppntmnt(Appntmnt appntmnt) {
        if (appntmnts.contains(appntmnt)) {
            return false;
        } else {
            appntmnts.add(appntmnt);
            return true;
        }
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
}
