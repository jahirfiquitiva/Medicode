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

import android.os.Parcel;
import android.os.Parcelable;

public class Appntmnt implements Parcelable {

    private Doctor doctor;
    private Patient patient;
    private String date;

    public Appntmnt(Doctor doctor, Patient patient, String date) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
    }

    protected Appntmnt(Parcel in) {
        doctor = in.readParcelable(Doctor.class.getClassLoader());
        patient = in.readParcelable(Patient.class.getClassLoader());
        date = in.readString();
    }

    public static final Creator<Appntmnt> CREATOR = new Creator<Appntmnt>() {
        @Override
        public Appntmnt createFromParcel(Parcel in) {
            return new Appntmnt(in);
        }

        @Override
        public Appntmnt[] newArray(int size) {
            return new Appntmnt[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(doctor, i);
        parcel.writeParcelable(patient, i);
        parcel.writeString(date);
    }
}