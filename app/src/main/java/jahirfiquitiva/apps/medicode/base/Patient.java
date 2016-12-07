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

public class Patient implements Parcelable {

    private String name;
    private String id;
    private boolean gender;
    private int age;

    public Patient(String name, String id, boolean gender, int age) {
        this.name = name;
        this.id = id;
        this.gender = gender;
        this.age = age;
    }

    protected Patient(Parcel in) {
        name = in.readString();
        id = in.readString();
        gender = in.readByte() != 0;
        age = in.readInt();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeByte((byte) (gender ? 1 : 0));
        parcel.writeInt(age);
    }
}