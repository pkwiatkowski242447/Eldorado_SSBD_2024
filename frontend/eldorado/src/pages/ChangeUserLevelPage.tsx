import {zodResolver} from "@hookform/resolvers/zod";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {Button} from "@/components/ui/button";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group";
import SiteHeader from "@/components/SiteHeader.tsx";
import {Card, CardHeader} from "@/components/ui/card.tsx";
import {useAccountState} from "@/context/AccountContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Slash} from "lucide-react";
import {Pathnames} from "@/router/pathnames.ts";

const FormSchema = z.object({
    type: z.any()
});

const roleNames = {
    ADMIN: "Admin",
    STAFF: "Staff",
    CLIENT: "Client",
};

const roleOrder = ["ADMIN", "STAFF", "CLIENT"];

function ChangeUserLevelPage() {
    const navigate = useNavigate();
    const {account} = useAccountState();
    const {getCurrentAccount} = useAccount();
    const {t} = useTranslation();

    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
    });

    useEffect(() => {
        getCurrentAccount();
    }, []);

    const orderedUserLevels = account?.userLevelsDto
        ?.slice()
        .sort((a, b) => roleOrder.indexOf(a.roleName) - roleOrder.indexOf(b.roleName)) || [];

    function onSubmit(data: z.infer<typeof FormSchema>) {
        try {
            if (account) {
                const newActiveUserLevel
                    = account.userLevelsDto.find((userLevel) => userLevel.roleName === data.type);
                if (newActiveUserLevel) {
                    account.activeUserLevel = newActiveUserLevel;
                    localStorage.setItem('account', JSON.stringify(account));
                    localStorage.setItem('chosenUserLevel', newActiveUserLevel.roleName);
                }
            }
            navigate(Pathnames.loggedIn.home);
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.changeUserlevel")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="flex justify-center items-center mx-auto p-10">
                <Card>
                    <CardHeader className="p-3 font-bold">
                        {t("siteHeader.changeLevel.select")}
                    </CardHeader>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5 p-5">
                            <FormField
                                control={form.control}
                                name="type"
                                render={({field}) => (
                                    <FormItem className="space-y-3">
                                        <FormControl>
                                            <RadioGroup
                                                onValueChange={field.onChange}
                                                defaultValue={account?.activeUserLevel?.roleName}
                                                className="flex flex-col space-y-1"
                                            >
                                                {orderedUserLevels.map((userLevel, index) => (
                                                    <FormItem key={index}
                                                              className="flex items-center space-x-3 space-y-0">
                                                        <FormControl>
                                                            <RadioGroupItem value={userLevel.roleName}/>
                                                        </FormControl>
                                                        <FormLabel className="font-normal">
                                                            {roleNames[userLevel.roleName]}
                                                        </FormLabel>
                                                    </FormItem>
                                                ))}
                                            </RadioGroup>
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                )}
                            />
                            <Button type="submit">{t("siteHeader.changeLevel.select.save")}</Button>
                        </form>
                    </Form>
                </Card>
            </div>
        </div>
    );
}

export default ChangeUserLevelPage;
