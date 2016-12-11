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

package jahirfiquitiva.apps.medicode.logic.objects;

import java.io.Serializable;

import jahirfiquitiva.apps.medicode.logic.ListsManager;
import jahirfiquitiva.apps.medicode.logic.enums.Gender;

public class Patient extends Person implements Serializable {

    private static final long serialVersionUID = 123L;

    private int age;
    private Gender gender;
    private String rh;
    private String eps;

    public Patient(String name, String id, int age, Gender gender, String rh, String eps) {
        super(name, id);
        this.age = age;
        this.gender = gender;
        this.rh = rh;
        this.eps = eps;
    }

    public String getName() {
        return ListsManager.makeNameCamelCase(super.getName());
    }

    public String getId() {
        return super.getId();
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getRh() {
        return rh;
    }

    public String getEps() {
        return eps;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof Patient && (this.getId().equals(((Patient)
                object).getId()));
    }

}