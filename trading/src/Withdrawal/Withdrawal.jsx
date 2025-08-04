import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow
} from "@/components/ui/table";
import { getWithdrawalHistory } from '@/State/Withdrawal/Action';
import { useEffect } from "react";
import { useDispatch, useSelector } from 'react-redux';

const Withdrawal = () => {

  const dispatch = useDispatch(); // Fixed useDispatch usage
  const { wallet, withdrawal } = useSelector(store => store);

  useEffect(() => {
    dispatch(getWithdrawalHistory(localStorage.getItem("jwt")));
  }, [dispatch]); 

  return (
    <div>
      <div className='p-5 lg:px-20'>
        <h1 className='font-bold text-3xl pb-5'>Withdrawal</h1>
        <Table className='border'>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[100px]">Date</TableHead>
              <TableHead>Method</TableHead>
              <TableHead>Amount</TableHead>
              <TableHead className='text-right'>Status</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {(withdrawal?.history || []).map((item, index) =>
              <TableRow key={index}>
                <TableCell>
                  <p>{item.date || "N/A"}</p>
                </TableCell>
                <TableCell>Bank</TableCell>
                <TableCell>${item.amount || "N/A"}</TableCell>
                <TableCell className='text-center'>
                  {item.status || "N/A"}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
    </div>
  );
}

export default Withdrawal