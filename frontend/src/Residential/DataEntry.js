import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage';
import Form from 'react-bootstrap/Form';
import './DataEntry.css'
import { Button } from 'react-bootstrap';
import { useState, useEffect } from 'react';
import axios from 'axios';
import PDFFile from './mau_phieu.pdf'

function DataEntry() {
    const [provinceCodeAddress1, setProvinceCodeAddress1] = useState();
    const [districtCodeAddress1, setDistrictCodeAddress1] = useState();
    const [wardCodeAddress1, setWardCodeAddress1] = useState();
    const [provinceCodeAddress2, setProvinceCodeAddress2] = useState();
    const [districtCodeAddress2, setDistrictCodeAddress2] = useState();
    const [wardCodeAddress2, setWardCodeAddress2] = useState();
    const [hamletCodeAddress2, setHamletCodeAddress2] = useState();
    const [provinceCodeAddress3, setProvinceCodeAddress3] = useState();
    const [districtCodeAddress3, setDistrictCodeAddress3] = useState();
    const [wardCodeAddress3, setWardCodeAddress3] = useState();
    const [hamletCodeAddress3, setHamletCodeAddress3] = useState();
    const [provincesAddress1, setProvincesAddress1] = useState([]);
    const [provincesAddress2, setProvincesAddress2] = useState([]);
    const [provincesAddress3, setProvincesAddress3] = useState([]);
    const [districtsAddress1, setDistrictsAddress1] = useState([]);
    const [districtsAddress2, setDistrictsAddress2] = useState([]);
    const [districtsAddress3, setDistrictsAddress3] = useState([]);
    const [wardsAddress1, setWardsAddress1] = useState([]);
    const [wardsAddress2, setWardsAddress2] = useState([]);
    const [wardsAddress3, setWardsAddress3] = useState([]);
    const [hamletsAddress2, setHamletsAddress2] = useState([]);
    const [hamletsAddress3, setHamletsAddress3] = useState([]);
    const [ethnicitys, setEthnicitys] = useState([]);
    const [bloodType, setBloodType] = useState();
    const [dateOfBirth, setDateOfBirth] = useState();
    const [ethnicity, setEthnicity] = useState();
    const [sex, setSex] = useState();
    const [marriageStatus, setMarriageStatus] = useState();
    const [religionId, setReligionId] = useState();
    const [religionName, setReligionName] = useState();

    let fullName = undefined;
    let fullNameAssociation1 = undefined;
    let fullNameAssociation2 = undefined;
    let fullNameAssociation3 = undefined;
    let fullNameAssociation4 = undefined;
    let nationalId = undefined;
    let nationalIdAssociation1 = undefined;
    let nationalIdAssociation2 = undefined;
    let nationalIdAssociation3 = undefined;
    let nationalIdAssociation4 = undefined;
    let otherNationality = undefined;

    const fetchProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/');
            setProvincesAddress1(response.data);
            setProvincesAddress2(response.data);
            setProvincesAddress3(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchEthnicity = async () => {
        try {
            const response = await axios(' http://localhost:8080/api/v1/ethnicity/');
            setEthnicitys(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchWard = async (code, index) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/districtCode/' + code);
            if (index === 1) setWardsAddress1(response.data);
            else if (index === 2) setWardsAddress2(response.data);
            else if (index === 3) setWardsAddress3(response.data);
        } catch (err) {
            console.error(err);
        }
    };


    const fetchDistrict = async (code, index) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/provinceCode/' + code);
            if (index === 1) setDistrictsAddress1(response.data);
            else if (index === 2) setDistrictsAddress2(response.data);
            else if (index === 3) setDistrictsAddress3(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchHamlet = async (code, index) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/hamlet/wardCode/' + code);
            if (index === 2) setHamletsAddress2(response.data);
            else if (index === 3) setHamletsAddress3(response.data);
            console.log(response.data)
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchProvince();
        fetchEthnicity()
    }, [])

    const listEthnicity = ethnicitys.map((post) =>
        <option key={post.id} value={post.id}>{post.id + ". " + post.name}</option>
    );

    const listProvincesAddress1 = provincesAddress1.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistrictsAddress1 = districtsAddress1.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWardsAddress1 = wardsAddress1.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listProvincesAddress2 = provincesAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistrictsAddress2 = districtsAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWardsAddress2 = wardsAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listHamletsAddress2 = hamletsAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
    );

    const listProvincesAddress3 = provincesAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistrictsAddress3 = districtsAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWardsAddress3 = wardsAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listHamletsAddress3 = hamletsAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
    );

    const OnClickButton = () => {
        console.log(fullNameAssociation1 + " " + nationalIdAssociation2)
    }

    const FormInput = (() => {
        return (
            <div>
                <a href={PDFFile} download="mau-phieu-thu-thap-thong-tin-dan-cu" target="_blank" rel="noreferrer">
                    <Button className="buttonPrint">Tải mẫu phiếu</Button>
                </a>
                <div className="findFlex">
                    <div className="flex">
                        <div className="textInput">1. Họ và tên: </div>
                        <Form.Control type="text" className='optionNameInput' value={fullName} onChange={(e) => fullName = e.target.value} />
                    </div>
                    <div className="flex">
                        <div className="textInput">2. Nhóm máu </div>
                        <Form.Select className='optionBloodTypeInput' value={bloodType} onChange={(e) => setBloodType(e.target.value)}>
                            <option></option>
                            <option value='A'>A</option>
                            <option value='B'>B</option>
                            <option value='AB'>AB</option>
                            <option value='O'>O</option>
                        </Form.Select>
                    </div>
                </div>
                <div className="findFlex">
                    <div className='flex'>
                        <div className="textInput">3. Ngày sinh: </div>
                        <Form.Control type="date" className='optionInput' value={dateOfBirth} onChange={(e) => setDateOfBirth(e.target.value)} />
                    </div>
                    <div className='flex'>
                        <div className="textInput">4. Giới tính </div>
                        <Form.Select className='optionInput' value={sex} onChange={(e) => setSex(e.target.value)}>
                            <option></option>
                            <option value={'Nam'}>Nam</option>
                            <option value={'Nữ'}>Nữ</option>
                        </Form.Select>
                    </div>
                    <div className='flex'>
                        <div className="textInput">5. Tình trạng hôn nhân</div>
                        <Form.Select className='optionInput' value={marriageStatus} onChange={(e) => setMarriageStatus(e.target.value)}>
                            <option></option>
                            <option value={'Chưa kết hôn'}>Chưa kết hôn</option>
                            <option value={'Đã kết hôn'}>Đã kết hôn</option>
                            <option value={'Đã ly hôn'}>Đã ly hôn</option>
                        </Form.Select>
                    </div>
                    <div className='flex'>
                        <div className="textInput">6. Dân tộc: </div>
                        <Form.Select className='optionInput' value={ethnicity} onChange={(e) => setEthnicity(e.target.value)}><option></option>{listEthnicity}</Form.Select>
                    </div>
                </div>
                <div className="findFlex">
                    <div className='flex'>
                        <div className="textInput">7. Tôn giáo: </div>
                        <Form.Control type="text" className='optionInput' value={religionId} key={religionName} onChange={(e) => {
                            setReligionId(e.target.value)
                            setReligionName(e.target.key)
                        }} />
                    </div>
                    <div className="flex">
                        <div className="textInput">8. Quốc tịch khác: </div>
                        <Form.Control type="text" value={otherNationality} className='optionNameInput' onChange={(e) => otherNationality = e.target.value} />
                    </div>
                    <div className="flex">
                        <div className="textInput">9. Số CCCD/CMND: </div>
                        <Form.Control type="number" value={nationalId} className='optionNameInput' onChange={(e) => nationalId = e.target.value} />
                    </div>
                </div>
                <div className="findFlex">
                    <div className='flex'>
                        <div className='titleInformation'>10. Thông tin của người thân (cha):</div>
                        <div className="textInput">Họ và tên </div>
                        <Form.Control type="text" className='optionInput' value={fullNameAssociation1} onChange={(e) => fullNameAssociation1 = e.target.value} />
                        <div className="textInput">Số CMMD/CCCD </div>
                        <Form.Control type="text" className='optionInput' value={nationalIdAssociation1} onChange={(e) => nationalIdAssociation1 = e.target.value} />
                    </div>
                    <div className="flex">
                        <div className='titleInformation'>11. Thông tin của người thân (mẹ):</div>
                        <div className="textInput">Họ và tên </div>
                        <Form.Control type="text" className='optionInput' value={fullNameAssociation1} onChange={(e) => fullNameAssociation2 = e.target.value} />
                        <div className="textInput">Số CMMD/CCCD </div>
                        <Form.Control type="text" className='optionInput' value={nationalIdAssociation1} onChange={(e) => nationalIdAssociation2 = e.target.value} />
                    </div>
                    <div className="flex">
                        <div className='titleInformation'>12. Thông tin của người thân (giám hộ):</div>
                        <div className="textInput">Họ và tên </div>
                        <Form.Control type="text" className='optionInput' value={fullNameAssociation1} onChange={(e) => fullNameAssociation3 = e.target.value} />
                        <div className="textInput">Số CMMD/CCCD </div>
                        <Form.Control type="text" className='optionInput' value={nationalIdAssociation1} onChange={(e) => nationalIdAssociation3 = e.target.value} />
                    </div>
                    <div className="flex">
                        <div className='titleInformation'>13. Thông tin của người thân (vợ / chồng):</div>
                        <div className="textInput">Họ và tên </div>
                        <Form.Control type="text" className='optionInput' value={fullNameAssociation1} onChange={(e) => fullNameAssociation4 = e.target.value} />
                        <div className="textInput">Số CMMD/CCCD </div>
                        <Form.Control type="text" className='optionInput' value={nationalIdAssociation1} onChange={(e) => nationalIdAssociation4 = e.target.value} />
                    </div>
                </div>
                <div className="findFlex" style={{ marginTop: '20px' }}>
                    <div className="flex_address">
                        <div className="titleAddress">14. Quê quán </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={provinceCodeAddress1} onChange={(e) => {
                                fetchDistrict(e.target.value, 1)
                                setProvinceCodeAddress1(e.target.value)
                                setWardsAddress1([])
                            }}>
                                <option></option>
                                {listProvincesAddress1}
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={districtCodeAddress1} onChange={(e) => {
                                fetchWard(e.target.value, 1)
                                setDistrictCodeAddress1(e.target.value)
                            }}>
                                <option></option>
                                {listDistrictsAddress1}
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={wardCodeAddress1} onChange={(e) => {
                                setWardCodeAddress1(e.target.value, 1)
                            }}>
                                <option></option>
                                {listWardsAddress1}
                            </Form.Select>
                        </div>
                    </div>
                    <div className="flex_address">
                        <div className="titleAddress">15. Địa chỉ thường trú </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={provinceCodeAddress2} onChange={(e) => {
                                fetchDistrict(e.target.value, 2)
                                setProvinceCodeAddress2(e.target.value)
                                setWardsAddress2([])
                                setHamletsAddress2([])
                            }}>
                                <option></option>
                                {listProvincesAddress2}
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={districtCodeAddress2} onChange={(e) => {
                                fetchWard(e.target.value, 2)
                                setDistrictCodeAddress2(e.target.value)
                                setHamletsAddress2([])
                            }}>
                                <option></option>
                                {listDistrictsAddress2}
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={wardCodeAddress2} onChange={(e) => {
                                fetchHamlet(e.target.value, 2)
                                setWardCodeAddress2(e.target.value)
                            }}>
                                <option></option>
                                {listWardsAddress2}
                            </Form.Select>
                            <div className="textInput">Thôn/Tổ dân phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={hamletCodeAddress2} onChange={(e) => {
                                setHamletCodeAddress2(e.target.value, 2)
                            }}>
                                <option></option>
                                {listHamletsAddress2}
                            </Form.Select>
                        </div>
                    </div>
                    <div className="flex_address">
                        <div className="titleAddress">16. Địa chỉ tạm trú </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={provinceCodeAddress3} onChange={(e) => {
                                fetchDistrict(e.target.value, 3)
                                setProvinceCodeAddress3(e.target.value)
                                setWardsAddress3([])
                                setHamletsAddress3([])
                            }}>
                                <option></option>
                                {listProvincesAddress3}
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={districtCodeAddress3} onChange={(e) => {
                                fetchWard(e.target.value, 3)
                                setDistrictCodeAddress3(e.target.value)
                                setHamletsAddress3([])
                            }}>
                                <option></option>
                                {listDistrictsAddress3}
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={wardCodeAddress3} onChange={(e) => {
                                fetchHamlet(e.target.value, 3)
                                setWardCodeAddress3(e.target.value)
                            }}>
                                <option></option>
                                {listWardsAddress3}
                            </Form.Select>
                            <div className="textInput">Thôn/Tổ dân phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={hamletCodeAddress3} onChange={(e) => {
                                setHamletCodeAddress3(e.target.value, 3)
                            }}>
                                <option></option>
                                {listHamletsAddress3}
                            </Form.Select>
                        </div>
                    </div>
                </div>
                <div className='acpButton'><Button onClick={() => OnClickButton()}>Xác nhận</Button></div>
            </div>
        )
    })

    return (
        <div>
            <NavbarPage />
            <FormInput />
        </div>
    );
}

export default DataEntry;
