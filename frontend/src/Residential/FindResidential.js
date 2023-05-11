import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage';
import Form from 'react-bootstrap/Form';
import './findResidential.css'
import { Button } from 'react-bootstrap';
import { useState, useEffect } from 'react';
import axios from 'axios';

function Residential() {

    const years = [];
    for (let i = 1900; i <= 2023; i++) {
        years.push(i)
    }

    const [provinceCode, setProvinceCode] = useState();
    const [districtCode, setDistrictCode] = useState();
    const [wardCode, setWardCode] = useState();
    const [provinces, setProvinces] = useState([]);
    const [districts, setDistricts] = useState([]);
    const [wards, setWards] = useState([]);

    const fetchProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/');
            setProvinces(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchWard = async (districtCode) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/districtCode/' + districtCode);
            setWards(response.data);
        } catch (err) {
            console.error(err);
        }
    };


    const fetchDistrict = async (provinceCode) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/provinceCode/' + provinceCode);
            setDistricts(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchProvince();
    }, [])

    const listItems = years.map((number) =>
        <option value={number} key={number}>{number}</option>
    );

    const listProvinces = provinces.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistricts = districts.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWards = wards.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const FormInput = (() => {
        return (
            <div>
                <div className="findFlex">
                    <div className="flex">
                        <div className="textInput">Họ và tên: </div>
                        <Form.Control type="text" className='optionInput' />
                    </div>
                    <div className="flex">
                        <div className="textInput">Số CMMD/CCCD: </div>
                        <Form.Control type="text" className='optionInput' />
                    </div>
                    <div className="flex">
                        <div className="textInput">Năm sinh: </div>
                        <Form.Select aria-label="Default select example" className='optionInputYear'>
                            {listItems}
                        </Form.Select>
                    </div>
                </div>

                <div className="findFlex">
                    <div className="flex_address">
                        <div className="titleAddress">11. Quê quán </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionInput' value={provinceCode} onChange={(e) => {
                                fetchDistrict(e.target.value)
                                setProvinceCode(e.target.value)
                                setWards([])
                            }}>
                                {listProvinces}
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionInput' value={districtCode} onChange={(e) => {
                                fetchWard(e.target.value)
                                setDistrictCode(e.target.value)
                            }}>
                                {listDistricts}
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionInput' value={wardCode} onChange={(e) => {
                                setWardCode(e.target.value)
                            }}>
                                {listWards}
                            </Form.Select>
                        </div>
                    </div>
                    <div className="flex_address">
                        <div className="titleAddress">12. Địa chỉ thường trú </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                            <div className="textInput">Thôn/Tổ dân phố: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                        </div>
                    </div>
                    <div className="flex_address">
                        <div className="titleAddress">13. Địa chỉ tạm trú </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                            <div className="textInput">Thôn/Tổ dân phố: </div>
                            <Form.Select aria-label="Default select example" className='optionInput'>
                            </Form.Select>
                        </div>
                    </div>
                </div>
                <div className='acpButton'><Button>Xác nhận</Button></div>
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

export default Residential;
