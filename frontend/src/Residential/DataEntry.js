import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage';
import Form from 'react-bootstrap/Form';
import './DataEntry.css'
import { Button } from 'react-bootstrap';
import { useState, useEffect } from 'react';
import axios from 'axios';
import PDFFile from './mau_phieu.pdf'

function DataEntry() {
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
                <a href={PDFFile} download="mau-phieu-thu-thap-thong-tin-dan-cu" target="_blank" rel="noreferrer">
                    <Button className="buttonPrint">Tải mẫu phiếu</Button>
                </a>
                <div className="findFlex">
                    <div className="flex">
                        <div className="textInput">1. Họ và tên: </div>
                        <Form.Control type="text" className='optionNameInput' />
                    </div>
                    <div className="flex">
                        <div className="textInput">2. Nhóm máu </div>
                        <Form.Select className='optionBloodTypeInput'>
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
                        <Form.Control type="date" className='optionInput' />
                    </div>
                    <div className='flex'>
                        <div className="textInput">4. Giới tính </div>
                        <Form.Select className='optionInput'>
                            <option value={0}>Nam</option>
                            <option value={1}>Nữ</option>
                        </Form.Select>
                    </div>
                    <div className='flex'>
                        <div className="textInput">5. Tình trạng hôn nhân</div>
                        <Form.Select className='optionInput'>
                            <option value={0}>Chưa kết hôn</option>
                            <option value={1}>Đã kết hôn</option>
                            <option value={2}>Đã ly hôn</option>
                        </Form.Select>
                    </div>
                </div>
                <div className="findFlex">
                    <div className='flex'>
                        <div className="textInput">6. Dân tộc: </div>
                        <Form.Control type="text" className='optionInput' />
                    </div>
                    <div className='flex'>
                        <div className="textInput">7. Tôn giáo </div>
                        <Form.Control type="text" className='optionInput' />
                    </div>
                    <div className='flex'>
                        <div className="textInput">8. Trình độ văn hóa</div>
                        <Form.Control type="text" className='optionInput' />
                    </div>
                </div>
                <div className="findFlex">
                    <div className="flex">
                        <div className="textInput">9. Quốc tịch khác: </div>
                        <Form.Control type="text" className='optionNameInput' />
                    </div>
                    <div className="flex">
                        <div className="textInput">10. Số CCCD/CMND: </div>
                        <Form.Control type="number" className='optionNameInput' />
                    </div>
                </div>
                <div className="findFlex" style={{ marginTop: '20px' }}>
                    <div className="flex_address">
                        <div className="titleAddress">11. Quê quán </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={provinceCode} onChange={(e) => {
                                fetchDistrict(e.target.value)
                                setProvinceCode(e.target.value)
                                setWards([])
                            }}>
                                {listProvinces}
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={districtCode} onChange={(e) => {
                                fetchWard(e.target.value)
                                setDistrictCode(e.target.value)
                            }}>
                                {listDistricts}
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput' value={wardCode} onChange={(e) => {
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
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                            <div className="textInput">Thôn/Tổ dân phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                        </div>
                    </div>
                    <div className="flex_address">
                        <div className="titleAddress">13. Địa chỉ tạm trú </div>
                        <div className="titleSelectOption">
                            <div className="textInput">Tỉnh/Thành phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                            <div className="textInput">Quận/huyện/thị xã: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                            <div className="textInput">Xã/phường/thị trấn: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                            <div className="textInput">Thôn/Tổ dân phố: </div>
                            <Form.Select aria-label="Default select example" className='optionSelectInput'>
                            </Form.Select>
                            <div className="textInput">Ghi chú (Số nhà/Ngõ): </div>
                            <Form.Control type="text" className='optionSelectInput' />
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

export default DataEntry;
