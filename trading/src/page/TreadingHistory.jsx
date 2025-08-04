/* eslint-disable no-unused-vars */
import { Avatar, AvatarImage } from "@/components/ui/avatar";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { getUserAssets } from "@/State/Assests/Action";
import { getAllOrdersForUser } from "@/State/Order/Action";




import { readableDate } from "@/utils/readableDate";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

const TreadingHistory = () => {
   const dispatch = useDispatch();
  const [currentTab, setCurrentTab] = useState("portfolio");
  const { asset, orders } = useSelector((store) => store.order);
  // const [activeTab, setActiveTab] = useState("portfolio");

  useEffect(() => {
    dispatch(getUserAssets(localStorage.getItem("jwt")));
    dispatch(getAllOrdersForUser({ jwt: localStorage.getItem("jwt") }));
  }, []);

  const handleTabChange = (value) => {
    setCurrentTab(value);
  };

   console.log("currentTab-----", currentTab);
  return (
    <div className="">
      <Table className="px-5  relative">
        <TableHeader className="py-9">
          <TableRow className="sticky top-0 left-0 right-0 bg-background ">
            <TableHead className="py-3">Date & Time</TableHead>
            <TableHead>Treading Pair</TableHead>
            <TableHead>Buy Price</TableHead>
            <TableHead>Selling Price</TableHead>
            <TableHead>Order Type</TableHead>
            <TableHead>Profite/Loss</TableHead>
            <TableHead className="text-right">VALUE</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody className="">
          {orders?.map((item) => (
  <TableRow key={item.id}>
    <TableCell>
      <p>{readableDate(item.timestamp).date}</p>
      <p className="text-gray-400">
        {readableDate(item.timestamp).time}
      </p>
    </TableCell>

    <TableCell className="font-medium flex items-center gap-2">
      <Avatar className="-z-50">
        <AvatarImage
          src={`https://cryptologos.cc/logos/${item.assetSymbol}-logo.png`} // optional fallback
          alt={item.assetSymbol}
        />
      </Avatar>
      <span> {item.assetSymbol?.toUpperCase()}</span>
    </TableCell>

    <TableCell>
      {item.orderType === "BUY" ? `$${item.price}` : "-"}
    </TableCell>

    <TableCell>
      {item.orderType === "SELL" ? `$${item.price}` : "-"}
    </TableCell>

    <TableCell>{item.orderType}</TableCell>

    <TableCell className="text-gray-500">-</TableCell> {/* Placeholder for profit/loss */}

    <TableCell className="text-right">${item.price}</TableCell>
  </TableRow>
))}

        </TableBody>
      </Table>
    </div>
  );
};

export default TreadingHistory;
