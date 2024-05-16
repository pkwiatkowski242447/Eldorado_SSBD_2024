import {SetStateAction, useEffect, useState} from 'react';
import SiteHeader from "@/components/SiteHeader.tsx";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useForm} from "react-hook-form";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {isValidPhoneNumber} from "react-phone-number-input/min";
import {PhoneInput} from "@/components/ui/phone-input.tsx";
import {useAccountState} from "@/context/AccountContext.tsx";
import {parsePhoneNumber} from "react-phone-number-input";
import {api} from "@/api/api.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";


function AccountSettings() {
    const [activeForm, setActiveForm] = useState('Authentication');
    const {account} = useAccountState();
    const {t} = useTranslation();
    const phoneNumber = parsePhoneNumber(account?.phone || '')
    const e164Number = phoneNumber?.format('E.164')
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [formValues, setFormValues] = useState(null);
    const [formType, setFormType] = useState(null);

    const emailSchema = z.object({
        email: z.string().email({message: t("accountSettings.wrongEmail")}),
    });

    const userDataSchema = z.object({
        name: z.string().min(2, {message: t("accountSettings.firstNameTooShort")})
            .max(50, {message: t("accountSettings.firstNameTooLong")}),
        lastName: z.string().min(2, {message: t("accountSettings.lastNameTooShort")})
            .max(50, {message: t("accountSettings.lastNameTooLong")}),
        phoneNumber: z.string().refine(isValidPhoneNumber, {message: t("accountSettings.phoneNumberInvalid")}),
    });

    const {getCurrentAccount} = useAccount();

    //It has to be that way afaik so ignore the warning and enjoy your day :)
    useEffect(() => {
        getCurrentAccount();
    }, []);


    const formEmail = useForm({
        resolver: zodResolver(emailSchema),
    });

    const formUserData = useForm({
        resolver: zodResolver(userDataSchema),
    });

    const onSubmitEmail = (values: SetStateAction<null>) => {
        setFormValues(values);
        // @ts-expect-error - fix this
        setFormType('email');
        setAlertDialogOpen(true);
    };

    const onSubmitUserData = (values: SetStateAction<null>) => {
        setFormValues(values);
        //@ts-expect-error - fix this
        setFormType('userData');
        setAlertDialogOpen(true);
    };

    const handleDialogAction = () => {
        if (formType === 'email') {
            // @ts-expect-error - fix this
            SubmitEmail(formValues);
        } else if (formType === 'userData') {
            // @ts-expect-error - fix this
            SubmitUserData(formValues);
        }
        setAlertDialogOpen(false);
    };

    const SubmitEmail = (values: z.infer<typeof emailSchema>) => {
        // console.log(values.email)
        api.changeEmailSelf(values.email).then(() => {
            getCurrentAccount();

            toast({
                title: t("accountSettings.popUp.changeEmailOK.title"),
                description: t("accountSettings.popUp.changeEmailOK.text"),
            });
            setFormType(null);
            setFormValues(null);

        }).catch((error) => {
            if (error.response && error.response.data) {
                const {message, violations} = error.response.data;
                const violationMessages = violations.map((violation: string | string[]) => t(violation)).join(", ");

                toast({
                    variant: "destructive",
                    title: t(message),
                    description: violationMessages,
                });
            } else {
                toast({
                    variant: "destructive",
                    description: "Error",
                });
            }
            // console.log(error.response ? error.response.data : error);
        });
    };

    const SubmitUserData = (values: z.infer<typeof userDataSchema>) => {
        const etag = window.localStorage.getItem('etag');
        if (account && account.accountLanguage && etag !== null) {
            api.modifyAccountSelf(account.login, account.version, account.userLevelsDto,
                values.name, values.lastName, values.phoneNumber, false, etag)
                .then(() => {
                    getCurrentAccount();
                    // window.location.reload()
                    toast({
                        title: t("accountSettings.popUp.changeUserDataOK.title"),
                        description: t("accountSettings.popUp.changeUserDataOK.text"),
                    });
                    setFormType(null);
                    setFormValues(null);

                }).catch((error) => {
                if (error.response && error.response.data) {
                    const {message, violations} = error.response.data;
                    const violationMessages = violations.map((violation: string | string[]) => t(violation)).join(", ");

                    toast({
                        variant: "destructive",
                        title: t(message),
                        description: violationMessages,
                    });
                } else {
                    toast({
                        variant: "destructive",
                        description: "Error",
                    });
                }
                // console.log(error.response ? error.response.data : error);
            });
        } else {
            console.log('Account or account language is not defined');
        }
    };

    return (
        <div>
            <SiteHeader/>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">{t("accountSettings.title")}</h1>
                </div>
                <div
                    className="mx-auto grid w-full max-w-6xl items-start gap-6 md:grid-cols-[180px_1fr] lg:grid-cols-[250px_1fr]">
                    <nav
                        className="grid gap-4 text-sm text-muted-foreground"
                    >
                        <a href="#" className="font-semibold text-primary"
                           onClick={() => setActiveForm('Authentication')}>
                            {t("accountSettings.authentication")}
                        </a>
                        <a href="#" className="font-semibold text-primary"
                           onClick={() => setActiveForm('Personal Info')}>{t("accountSettings.personalInfo")}</a>

                    </nav>
                    <div className="grid gap-6">
                        {activeForm === 'Authentication' && (
                            <div>
                                <Card className="mx-10 w-auto">
                                    <CardContent>
                                        <Form {...formEmail}>
                                            {// @ts-expect-error - fix this
                                                <form onSubmit={formEmail.handleSubmit(onSubmitEmail)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-5">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formEmail.control}
                                                                name="email"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.authentication.email")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={account?.email} {...field} />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}/>
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2">
                                                            {t("accountSettings.authentication.email.change")}
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("accountSettings.confirmEmailChange")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                        {activeForm === 'Personal Info' && (
                            <div>
                                <Card className="mx-auto">
                                    <CardContent>
                                        <Form {...formUserData}>
                                            {// @ts-expect-error - fix this
                                                <form onSubmit={formUserData.handleSubmit(onSubmitUserData)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-10">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="name"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.personalInfo.firstName")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={account?.name} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="lastName"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.personalInfo.lastName")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={account?.lastname} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="phoneNumber"
                                                                render={({field}) => (
                                                                    <FormItem className="items-start">
                                                                        <FormLabel
                                                                            className="text-black text-center">{t("accountSettings.personalInfo.phoneNumber")}</FormLabel>
                                                                        <FormControl className="w-full">
                                                                            <PhoneInput //TODO fix this
                                                                                placeholder={e164Number} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2">
                                                            {t("accountSettings.personalInfo.saveChanges")}
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("accountSettings.confirmUserDataChange")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}

export default AccountSettings;