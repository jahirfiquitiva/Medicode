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

package jahirfiquitiva.apps.medicode.logic.objects;

import java.io.Serializable;
import java.util.Comparator;

public class Appntmnt implements Serializable {

    private static final long serialVersionUID = 123L;

    public static Comparator<Appntmnt> appntmntComparator = new Comparator<Appntmnt>() {
        @Override
        public int compare(Appntmnt appntmnt, Appntmnt t1) {
            return appntmnt.getDate().compareTo(t1.getDate());
        }
    };

    private Doctor doctor;
    private Patient patient;
    private String date;

    public Appntmnt(Doctor doctor, Patient patient, String date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof Appntmnt && (this.date.equals(((Appntmnt)
                object).getDate()));
    }

}